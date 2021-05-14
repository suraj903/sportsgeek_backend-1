package com.project.sportsgeek.controller;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.model.PrivateChat;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.PrivateChatService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping(path = "/private-chat",produces = MediaType.APPLICATION_JSON_VALUE)
public class PrivateChatController {
    @Autowired
    PrivateChatService privateChatService;

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = PrivateChat.class),
                    @ApiResponse(code = 404, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/users/{userid1}/{userid2}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrivateChat>> getPrivateChatByUserId(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int userid1, @PathVariable @Valid @Pattern(regexp = "[0-9]*") int userid2) throws Exception {
        Result<List<PrivateChat>> privateChatList = privateChatService.findPrivateChatByUserId(userid1, userid2);
        return new ResponseEntity<>(privateChatList.getData(), HttpStatus.valueOf(privateChatList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = PrivateChat.class),
                    @ApiResponse(code = 400, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrivateChat> addPrivateChat(@RequestBody(required = true) @Valid PrivateChat privateChat) throws Exception {
        Result<PrivateChat> privateChatResult = privateChatService.addPrivateChat(privateChat);
        return new ResponseEntity(privateChatResult.getData(), HttpStatus.valueOf(privateChatResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = PrivateChat.class),
                    @ApiResponse(code = 400, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrivateChat> updatePrivateChat(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@RequestBody(required = true) @Valid PrivateChat privateChat) throws Exception {
        Result<PrivateChat> privateChatResult = privateChatService.updatePrivateChat(id, privateChat);
        return new ResponseEntity(privateChatResult.getData(), HttpStatus.valueOf(privateChatResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = PrivateChat.class),
                    @ApiResponse(code = 404, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = ResultException.class)
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<PrivateChat>> deletePrivateChatById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<Integer> integerResult =  privateChatService.deletePrivateChat(id);
        return new ResponseEntity(integerResult,HttpStatus.valueOf(integerResult.getCode()));
    }
}
