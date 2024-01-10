package com.xalpol12.messengerbot.crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDTO;
import com.xalpol12.messengerbot.crud.model.dto.scheduledmessage.ScheduledMessageDetails;
import com.xalpol12.messengerbot.crud.service.ScheduledMessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduledMessageController.class)
class ScheduledMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduledMessageService messageService;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void getScheduledMessage_returnScheduledMessage_expect200() throws Exception {
        long id = 1L;
        String endpointPath = ScheduledMessageController.ScheduledMessagePath.ROOT + "/" + id;
        ScheduledMessageDTO expected = getSampleDTO();

        when(messageService.getScheduledMessage(id)).thenReturn(expected);

        this.mockMvc
                .perform(get(endpointPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(asJsonString(expected)));
    }

    @Test
    public void getAllScheduledMessages_returnAll_expect200() throws Exception {
        String endpointPath = ScheduledMessageController.ScheduledMessagePath.ROOT + "s";
        List<ScheduledMessageDTO> expectedList = List.of(getSampleDTO(), getSampleDTO());

        when(messageService.getAllScheduledMessages()).thenReturn(expectedList);

        this.mockMvc
                .perform(get(endpointPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(asJsonString(expectedList)));
    }

    @Test
    public void uploadScheduledMessage_expect201() throws Exception {
        String endpointPath = ScheduledMessageController.ScheduledMessagePath.ROOT;
        ScheduledMessageDetails input = ScheduledMessageDetails.builder().imageId("1").build();
        ScheduledMessageDTO expected = getSampleDTO();

        when(messageService.addScheduledMessage(input)).thenReturn(expected);

        this.mockMvc
                .perform(post(endpointPath)
                        .content(asJsonString(input))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(asJsonString(expected)));
    }

    @Test
    public void deleteScheduledMessage_expect204() throws Exception {
        long id = 1L;
        String endpointPath = ScheduledMessageController.ScheduledMessagePath.ROOT + "/" + id;

        this.mockMvc
                .perform(delete(endpointPath))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAllScheduledMessages_expect204() throws Exception {
        String endpointPath = ScheduledMessageController.ScheduledMessagePath.ROOT + "s";

        this.mockMvc
                .perform(delete(endpointPath))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateScheduledMessage_expect200() throws Exception {
        long id = 1L;
        ScheduledMessageDetails input = getSampleMessageDetails();
        String endpointPath = ScheduledMessageController.ScheduledMessagePath.ROOT + "/" + id;

        this.mockMvc
                .perform(put(endpointPath)
                        .content(asJsonString(input))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ScheduledMessageDetails getSampleMessageDetails() {
        return ScheduledMessageDetails.builder().imageId("1").build();
    }
    private ScheduledMessageDTO getSampleDTO() {
        return ScheduledMessageDTO.builder()
                .scheduledMessageId(1L)
                .message("sample message")
                .isSent(false)
                .build();
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}