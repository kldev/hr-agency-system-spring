package com.pl.hragency.recruitment.domain.result;

import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.application.query.CandidateTagItem;

import java.util.List;

public record CandidateDetailsResult(CandidateItem candidate, List<CandidateTagItem> tags){
}
