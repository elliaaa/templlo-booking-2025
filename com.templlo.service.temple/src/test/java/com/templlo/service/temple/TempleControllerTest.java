package com.templlo.service.temple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templlo.service.temple.common.response.ApiResponse;
import com.templlo.service.temple.controller.TempleController;
import com.templlo.service.temple.dto.CreateTempleRequest;
import com.templlo.service.temple.dto.TempleResponse;
import com.templlo.service.temple.external.client.UserClient;
import com.templlo.service.temple.external.dto.UserData;
import com.templlo.service.temple.service.TempleService;
import com.templlo.service.temple.service.elasticsearch.TempleSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class TempleControllerTest {

    @Mock
    private TempleSearchService templeSearchService;

    @InjectMocks
    private TempleController templeController;

    @Mock
    private TempleService templeService;

    @Mock
    private UserClient userClient;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TempleController(templeService,templeSearchService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createTemple_Success() throws Exception {
        // Given
        String loginId = "test-login-id";
        String role = "TEMPLE_ADMIN";
        UUID userId = UUID.randomUUID();

        TempleResponse templeResponse = new TempleResponse(
                userId,
                "무선사",
                "제주 동쪽 김녕리에 자리한 금룡사는 너럭바위와 소나무 등이 어우러져 청룡, 황룡의 조화를 이루는 천연도량입니다. 본사의 지면 밑으로는 미로로 된 동굴이 뻗어 있고, 한라산 진맥에서 나오는 생수가 흘러 마을의 게웃샘을 거쳐 성세기물까지 이어집니다.",
                "010-0000-0000",
                "제주특별자치도 제주시 구좌읍 김녕로 148-11",
                "지하1층"
        );

        UserData userResponse = new UserData(
                userId,
                loginId,
                "test-email",
                "test-user",
                "test-nickname",
                "MALE",
                "1990-01-01",
                role,
                "010-1234-5678",
                5
        );

        ApiResponse<UserData> userDataApiResponse = ApiResponse.ofSuccess(userResponse);
        when(userClient.getUserInfo(loginId)).thenReturn(userDataApiResponse);
        when(templeService.createTemple(any(), any())).thenReturn(templeResponse);

        // 요청 본문 데이터 생성
        CreateTempleRequest request = CreateTempleRequest.builder()
                .templeName("무선사")
                .roadAddress("제주특별자치도 제주시 구좌읍 김녕로 148-11")
                .detailAddress("지하1층")
                .templeDescription("제주 동쪽 김녕리에 자리한 금룡사는 너럭바위와 소나무 등이 어우러져 청룡, 황룡의 조화를 이루는 천연도량입니다. 본사의 지면 밑으로는 미로로 된 동굴이 뻗어 있고, 한라산 진맥에서 나오는 생수가 흘러 마을의 게웃샘을 거쳐 성세기물까지 이어집니다.")
                .templePhone("010-0000-0000")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/temples")
                        .header("X-Login-Id", loginId)
                        .header("X-User-Role", role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.templeName").value("무선사"))
                .andExpect(jsonPath("$.data.templeDescription").value("제주 동쪽 김녕리에 자리한 금룡사는 너럭바위와 소나무 등이 어우러져 청룡, 황룡의 조화를 이루는 천연도량입니다. 본사의 지면 밑으로는 미로로 된 동굴이 뻗어 있고, 한라산 진맥에서 나오는 생수가 흘러 마을의 게웃샘을 거쳐 성세기물까지 이어집니다."))
                .andExpect(jsonPath("$.data.templePhone").value("010-0000-0000"));
    }


//    @Test
//    @WithMockUser(username = "testUser", roles = "TEMPLE_ADMIN")
//    void updateTemple_Success() throws Exception {
//        // Given
//        UUID templeId = UUID.randomUUID();
//        UpdateTempleRequest updateRequest = UpdateTempleRequest.builder()
//                .templeName("Updated Temple Name")
//                .build();
//
//        TempleResponse templeResponse = TempleResponse.builder()
//                .templeName("Updated Temple Name")
//                .templeDescription("Updated Description")
//                .templePhone("987-654")
//                .roadAddress("Updated Road")
//                .detailAddress("Updated Detail")
//                .build();
//
//        when(templeService.updateTemple(eq(templeId), any(), any())).thenReturn(templeResponse);
//
//        // When & Then
//        mockMvc.perform(patch("/api/temples/{templeId}", templeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.templeName").value("Updated Temple Name"));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "MASTER")
//    void deleteTemple_Success() throws Exception {
//        // Given
//        UUID templeId = UUID.randomUUID();
//
//        doNothing().when(templeService).deleteTemple(eq(templeId), any());
//
//        // When & Then
//        mockMvc.perform(delete("/api/temples/{templeId}", templeId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "TEMPLE_ADMIN")
//    void checkTempleOwnership_Success() throws Exception {
//        // Given
//        UUID templeId = UUID.randomUUID();
//
//        doNothing().when(templeService).validateTempleAdmin(eq(templeId), any());
//
//        // When & Then
//        mockMvc.perform(get("/api/temples/{templeId}/validate-admin", templeId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "TEMPLE_ADMIN")
//    void getTemplesByRegion_Success() throws Exception {
//        // Given
//        String region = "Seoul";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        PageResponse<TempleResponse> pageResponse = new PageResponse<>(new ArrayList<>(), 0, 10, 1);
//
//        when(templeService.getTemplesByRegion(eq(region), eq(pageable))).thenReturn(pageResponse);
//
//        // When & Then
//        mockMvc.perform(get("/api/temples/region")
//                        .param("region", region)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content.length()").value(0));
//    }
//
//    @Test
//    @WithMockUser(username = "testUser", roles = "TEMPLE_ADMIN")
//    void searchTemples_Success() throws Exception {
//        // Given
//        String keyword = "Temple";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        PageResponse<TempleResponse> pageResponse = new PageResponse<>(new ArrayList<>(), 0, 10, 1);
//
//        when(templeSearchService.searchTemples(eq(keyword), eq(pageable))).thenReturn(pageResponse);
//
//        // When & Then
//        mockMvc.perform(get("/api/temples/search")
//                        .param("keyword", keyword)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.content.length()").value(0));
//    }
}
