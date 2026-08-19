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

        'http_req_duration{endpoint:job-applications}': ['p(95)<500'],

        'http_req_duration{endpoint:candidates-no-filter}': ['p(95)<500'],
        'http_req_duration{endpoint:candidates-search}': ['p(95)<500'],
        'http_req_duration{endpoint:candidates-tags}': ['p(95)<500'],
        'http_req_duration{endpoint:candidates-search-tags}': ['p(95)<500'],
    },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const TAGS = [
    '20000000-0000-0000-0000-000000000001', // Java
    '20000000-0000-0000-0000-000000000002', // C#
    '20000000-0000-0000-0000-000000000003', // JavaScript
    '20000000-0000-0000-0000-000000000004', // TypeScript
    '20000000-0000-0000-0000-000000000005', // Python
    '20000000-0000-0000-0000-000000000006', // PHP
    '20000000-0000-0000-0000-000000000007', // Kotlin
    '20000000-0000-0000-0000-000000000008', // SQL
    '20000000-0000-0000-0000-000000000009', // PostgreSQL
    '20000000-0000-0000-0000-000000000010', // MySQL
    '20000000-0000-0000-0000-000000000011', // Docker
    '20000000-0000-0000-0000-000000000012', // Kubernetes
    '20000000-0000-0000-0000-000000000013', // AWS
    '20000000-0000-0000-0000-000000000014', // Azure
    '20000000-0000-0000-0000-000000000015', // Git
    '20000000-0000-0000-0000-000000000016', // Linux
    '20000000-0000-0000-0000-000000000017', // Spring Boot
    '20000000-0000-0000-0000-000000000018', // React
    '20000000-0000-0000-0000-000000000019', // Angular
    '20000000-0000-0000-0000-000000000020', // .NET
];

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

const SEARCH_VALUES = [
    'Java',
    'net',
];

function randomTag() {
    return randomItem(TAGS);
}

function randomTags() {
    const first = randomTag();

    // Jeden tag
    if (Math.random() < 0.5) {
        return first;
    }

    // Dwa różne tagi
    let second = randomTag();

    while (second === first) {
        second = randomTag();
    }

    return `${first},${second}`;
}

function randomSearch() {
    return randomItem(SEARCH_VALUES);
}

function createRandomCandidate(token) {
    const email = `candidate-${__VU}-${__ITER}-${Date.now()}@load-test.local`;

    const payload = JSON.stringify({
        email,
        firstName: `LoadTest${__VU}`,
        lastName: `Candidate${__ITER}`,
        phone: `+48123${String(__VU).padStart(2, '0')}${String(__ITER).padStart(4, '0')}`,
        source: randomSource(),
    });

    const response = http.post(
        `${BASE_URL}/api/recruitment/candidates`,
        payload,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            tags: {
                endpoint: 'candidate-create',
            },
        }
    );

    const success = check(response, {
        'candidate create status is 200': (r) => r.status === 200,
    });

    if (!success) {
        console.error(
            `CANDIDATE CREATE ERROR: status=${response.status}, body=${response.body}`
        );
    }

    return response;
}


/**
 * Losuje wariant zapytania:
 *
 * 1. bez filtrów
 * 2. search
 * 3. tags
 * 4. search + tags
 *
 * Rozkład:
 * 30% no filter
 * 30% search
 * 25% tags
 * 15% search + tags
 */
function candidateRequest() {
    const probability = Math.random();

    if (probability < 0.30) {
        return {
            query: '',
            tag: 'candidates-no-filter',
        };
    }

    if (probability < 0.60) {
        return {
            query: `&search=${encodeURIComponent(randomSearch())}`,
            tag: 'candidates-search',
        };
    }

    if (probability < 0.85) {
        return {
            query: `&tags=${randomTags()}`,
            tag: 'candidates-tags',
        };
    }

    return {
        query:
            `&search=${encodeURIComponent(randomSearch())}` +
            `&tags=${randomTags()}`,
        tag: 'candidates-search-tags',
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

function readJobApplications(token) {
    const response = http.get(
        `${BASE_URL}/api/recruitment/job-applications?page=0&size=20`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },

            tags: {
                endpoint: 'job-applications',
            },
        },
    );

  check(response, {
    'job applications status is 200': (r) => {
        if (r.status !== 200) {
            console.error(
                `JOB APPLICATIONS ERROR: status=${r.status}, body=${r.body}`
            );
        }

        return r.status === 200;
    },
});

    return response;
}

function readCandidates(token) {
    const request = candidateRequest();

    const response = http.get(
        `${BASE_URL}/api/recruitment/candidates?page=0&size=20${request.query}`,
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
                `CANDIDATES ERROR: variant=${request.tag}, status=${r.status}, body=${r.body}`
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

    // Użytkownik po zalogowaniu ogląda aplikacje
    readJobApplications(token);

    sleep(randomIntBetween(100, 300) / 1000);

    // Następnie wyszukuje/przegląda kandydatów
    readCandidates(token);

    // tworzenie kanydatów
    createRandomCandidate(token);

    // Mała przerwa między kolejnymi akcjami użytkownika
    sleep(randomIntBetween(200, 700) / 1000);
}