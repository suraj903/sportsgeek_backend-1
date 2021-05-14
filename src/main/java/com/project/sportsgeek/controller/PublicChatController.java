package com.project.sportsgeek.controller;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.PublicChat;
import com.project.sportsgeek.model.PublicChatWithUser;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.PublicChatService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping(path = "/public-chat",produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicChatController {

    @Autowired
    PublicChatService publicChatService;

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = PublicChat.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<List<PublicChatWithUser>>> getAllPublicChat() {
        Result<List<PublicChatWithUser>> publicChatList = publicChatService.findAllPublicChat();
        return new ResponseEntity<>(publicChatList, HttpStatus.valueOf(publicChatList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = PublicChat.class),
                    @ApiResponse(code = 404, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<PublicChatWithUser>> getPublicChatById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<PublicChatWithUser> publicChatList = publicChatService.findPublicChatById(id);
        return new ResponseEntity<>(publicChatList, HttpStatus.valueOf(publicChatList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = PublicChat.class),
                    @ApiResponse(code = 400, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<PublicChat>> addPublicChat(@RequestBody(required = true) @Valid PublicChat publicChat) throws Exception {
        Result<PublicChat> publicChatResult = publicChatService.addPublicChat(publicChat);
        return new ResponseEntity(publicChatResult, HttpStatus.valueOf(publicChatResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = PublicChat.class),
                    @ApiResponse(code = 400, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<PublicChat>> updatePublicChat(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@RequestBody(required = true) @Valid PublicChat publicChat) throws Exception {
        Result<PublicChat> publicChatResult = publicChatService.updatePublicChat(id, publicChat);
        return new ResponseEntity(publicChatResult,HttpStatus.valueOf(publicChatResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = PublicChat.class),
                    @ApiResponse(code = 404, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<PublicChat>> deletePublicChatById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<Integer> integerResult =  publicChatService.deletePublicChat(id);
        return new ResponseEntity(integerResult,HttpStatus.valueOf(integerResult.getCode()));
    }
}
