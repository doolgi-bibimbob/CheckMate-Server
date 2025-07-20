//package com.seonlim.mathreview.problem.service;
//
//import com.seonlim.mathreview.dto.ProblemFilterRequest;
//import com.seonlim.mathreview.dto.ProblemFilterResponse;
//import com.seonlim.mathreview.repository.ProblemRepository;
//import com.seonlim.mathreview.service.ProblemService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@Slf4j
//@ExtendWith(SpringExtension.class)
//class ProblemServiceTest {
//    @InjectMocks
//    private ProblemService problemService;
//
//    @Mock
//    private ProblemRepository problemRepository;
//
//    private final ProblemFilterResponse sample1 = new ProblemFilterResponse(
//            1L, "함수와 그래프", 2025, "2025 수능", List.of("함수"), 70.0
//    );
//
//    private final ProblemFilterResponse sample2 = new ProblemFilterResponse(
//            2L, "수열의 일반항", 2025, "2025 수능", List.of("수열"), 40.0
//    );
//
//    private final ProblemFilterResponse sample3 = new ProblemFilterResponse(
//            3L, "미분의 활용", 2024, "2024 수능", List.of("미분"), 90.0
//    );
//
//    @Test
//    void 필터링_제목_포함조건() {
//        ProblemFilterRequest request = new ProblemFilterRequest("함수", null, null, null);
//        when(problemRepository.filterProblems("함수", null, null, null))
//                .thenReturn(List.of(sample1));
//
//        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
//
//        log.info("[🔍 필터: 제목] title={}, resultCount={}", request.title(), result.size());
//
//        assertEquals(1, result.size());
//        assertEquals("함수와 그래프", result.get(0).title());
//    }
//
//    @Test
//    void 필터링_연도_조건() {
//        ProblemFilterRequest request = new ProblemFilterRequest(null, 2025, null, null);
//        when(problemRepository.filterProblems(null, 2025, null, null))
//                .thenReturn(List.of(sample1, sample2));
//
//        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
//
//        log.info("[🔍 필터: 연도] year={}, resultCount={}", request.year(), result.size());
//
//        assertEquals(2, result.size());
//        assertTrue(result.stream().allMatch(r -> r.year() == 2025));
//    }
//
//    @Test
//    void 필터링_태그_조건() {
//        ProblemFilterRequest request = new ProblemFilterRequest(null, null, "미분", null);
//        when(problemRepository.filterProblems(null, null, "미분", null))
//                .thenReturn(List.of(sample3));
//
//        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
//
//        log.info("[🔍 필터: 태그] tagName={}, resultTags={}, resultCount={}",
//                request.tagName(), result.get(0).tagNames(), result.size());
//
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).tagNames().contains("미분"));
//    }
//
//    @Test
//    void 필터링_정답률_최소_조건() {
//        ProblemFilterRequest request = new ProblemFilterRequest(null, null, null, 80.0);
//        when(problemRepository.filterProblems(null, null, null, 80.0))
//                .thenReturn(List.of(sample3));
//
//        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
//
//        log.info("[🔍 필터: 정답률] minAccuracyRate={}%, resultCount={}",
//                request.minAccuracyRate(), result.size());
//
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).accuracyRate() >= 80.0);
//    }
//
//    @Test
//    void 필터링_모든조건_복합_검색() {
//        ProblemFilterRequest request = new ProblemFilterRequest("수열", 2025, "수열", 30.0);
//        when(problemRepository.filterProblems("수열", 2025, "수열", 30.0))
//                .thenReturn(List.of(sample2));
//
//        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
//
//        log.info("[🔍 필터: 복합] title={}, year={}, tag={}, accRate>={}, resultCount={}",
//                request.title(), request.year(), request.tagName(), request.minAccuracyRate(), result.size());
//
//        assertEquals(1, result.size());
//        assertEquals(2L, result.get(0).problemId());
//    }
//}