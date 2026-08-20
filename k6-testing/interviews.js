import http from 'k6/http';
import { check, sleep } from 'k6';
import {
    randomIntBetween,
    randomItem,
} from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
    scenarios: {
        recruitment_read: {
            executor: 'constant-vus',
            vus: 50,
            duration: '2m',
        },
    },

    thresholds: {
        'http_req_failed': ['rate<0.01'],

        'http_req_duration{endpoint:login}': ['p(95)<500'],

        'http_req_duration{endpoint:job-posting}': ['p(95)<500'],
        'http_req_duration{endpoint:job-applications-apply}': ['p(95)<500'],
        'http_req_duration{endpoint:interview-only-mine}': ['p(95)<500'],
        'http_req_duration{endpoint:interview-no-filter}': ['p(95)<500'],
        'http_req_duration{endpoint:interview-period}': ['p(95)<500'],
        'http_req_duration{endpoint:interview-period-3months}': ['p(95)<500'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const CANDIDATE_SOURCES = [
    'CAREER_PAGE',
    'PRACUJ_PL',
    'OLX',
    'PRACA_PL',
    'ROCKETJOBS',
    'JUST_JOIN_IT',
    'NO_FLUFF_JOBS',
    'LINKEDIN',
    'INDEED',
    'REFERRAL',
    'DIRECT_SOURCING',
    'INTERNAL_DATABASE',
    'RECRUITMENT_AGENCY',
    'DIRECT_APPLICATION',
    'FACEBOOK',
    'OTHER',
    'DIRECT',
];

function randomSource() {
    return CANDIDATE_SOURCES[
        Math.floor(Math.random() * CANDIDATE_SOURCES.length)
    ];
}


function createInterviewCandidate(token, jobPostingId) {

    if (!jobPostingId)
    {
         console.error(
            `CREATE INTERVIEW jobPostingId is invalid`
        );
        return;
    }


    const email = `candidate-${__VU}-${__ITER}-${Date.now()}@load-test.local`;

    const payload = JSON.stringify({
        email,
        firstName: `LoadTest${__VU}`,
        lastName: `Candidate${__ITER}`,
        phone: `+48123${String(__VU).padStart(2, '0')}${String(__ITER).padStart(4, '0')}`,
        source: randomSource(),
        jobPostingId: jobPostingId

    });

    const response = http.post(
        `${BASE_URL}/api/recruitment/job-applications`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            tags: {
                endpoint: 'job-applications-apply',
            },
        }
    );

    const success = check(response, {
        'apply for job application status is 200': (r) => r.status === 200,
    });

    if (!success) {
        console.error(
            `APPLY FOR JOB ERROR: status=${response.status}, body=${response.body}`
        );
    }

    return response;
}

function randomDateBetween(from, to) {
    const fromTime = new Date(from).getTime();
    const toTime = new Date(to).getTime();

    const randomTime =
        fromTime + Math.random() * (toTime - fromTime);

    return new Date(randomTime).toISOString();
}

function createInterview(token, applicationId) {

    if (!applicationId)
    {
         console.error(
            `SCHEDULE INTERVIEW applicationId is invalid`
        );
        return;
    }

    const scheduledAt = randomDateBetween(
        '2027-07-01T08:00:00+02:00',
        '2027-10-01T17:00:00+02:00'
    );

    const payload = JSON.stringify({
        scheduledAt,
        scheduledTimezone: 'Europe/Warsaw',
    });

    const response = http.post(
        `${BASE_URL}/api/recruitment/job-applications/${applicationId}/schedule-interview`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            tags: {
                endpoint: 'job-applications-schedule-interview',
            },
        }
    );

    const success = check(response, {
        'schedule interview status is 200': (r) => r.status === 200,
    });

    if (!success) {
        console.error(
            `SCHEDULE INTERVIEW ERROR: status=${response.status}, body=${response.body}`
        );
    }

    return response;
}

function interviewRequest() {
    const probability = Math.random();

    if (probability < 0.30) {
        return {
            query: '',
            tag: 'interview-no-filter',
        };
    }

    if (probability < 0.60) {
        return {
            query: '&onlyMine=true',
            tag: 'interview-only-mine',
        };
    }

    if (probability < 0.85) {
        return {
            query: '&from=2027-09-01&to=2027-10-01',
            tag: 'interview-period',
        };
    }

    return {
        query: '&from=2027-07-01&to=2027-10-01',
        tag: 'interview-period-3months',
    };
}

function login() {
    const userNo = randomIntBetween(1, 99);

    const response = http.post(
        `${BASE_URL}/api/auth/login`,
        JSON.stringify({
            email: `admin${userNo}@performance.test`,
            password: 'performence-test',
            orgSlug: `performance-org-${userNo}`,
        }),
        {
            headers: {
                'Content-Type': 'application/json',
            },

            tags: {
                endpoint: 'login',
            },
        },
    );

    const loginSuccessful = check(response, {
        'login status is 200': (r) => r.status === 200,

        'login contains token': (r) => {
            try {
                return Boolean(r.json('token'));
            } catch {
                console.error(`Invalid login response: ${r.body}`);
                return false;
            }
        },
    });

    if (!loginSuccessful) {
        return null;
    }

    return response.json('token');
}

function readInterwiev(token) {
    const request = interviewRequest();

    const response = http.get(
        `${BASE_URL}/api/recruitment/interviews?page=0&size=20${request.query}`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },  

            tags: {
                endpoint: request.tag,
            },
        },
    );

    check(response, {
        [`${request.tag} status is 200`]: (r) => {
            if (r.status !== 200) {
                console.error(
                    `INTERVIEWS QUERY ERROR: variant=${request.tag}, status=${r.status}, body=${r.body}`
                );
            }

            return r.status === 200;
        },
    });

    return response;
}

function readJobPostings(token) {
    const response = http.get(
        `${BASE_URL}/api/recruitment/job-posting?page=0&size=20`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
            tags: {
                endpoint: 'job-posting',
            },
        },
    );

    check(response, {
        'job posting status is 200': (r) => {
            if (r.status !== 200) {
                console.error(
                    `JOB POSTING QUERY ERROR: status=${r.status}, body=${r.body}`
                );
            }

            return r.status === 200;
        },
    });

    return response;
}


export default function () {
    const token = login();

    if (!token) {
        return;
    }

    const postingResponse = readJobPostings(token);

    const postings = postingResponse.json('content');

    if (!postings || postings.length === 0) {
        console.error('No job postings available');
        return;
    }

    const postingId =
        postings[Math.floor(Math.random() * postings.length)].id;
    
    sleep(randomIntBetween(200, 700) / 1000);

    const applicationResponse =
        createInterviewCandidate(token, postingId);

    if (applicationResponse.status !== 200) {
        return;
    }

    const applicationId =
        applicationResponse.json('applicationId');

    sleep(randomIntBetween(200, 700) / 1000);

    createInterview(token, applicationId);

    sleep(randomIntBetween(200, 700) / 1000);

    readInterwiev(token);
}