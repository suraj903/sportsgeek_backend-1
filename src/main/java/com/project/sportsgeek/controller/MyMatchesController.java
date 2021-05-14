package com.project.sportsgeek.controller;

import com.project.sportsgeek.exception.MyMatchesException;
import com.project.sportsgeek.model.MyMatches;
import com.project.sportsgeek.model.Venue;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.MyMatchesService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@Controller
//@RequestMapping(path = "/myMatches",produces = MediaType.APPLICATION_JSON_VALUE)
public class MyMatchesController {

    @Autowired
    MyMatchesService myMatchesService;

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = MyMatches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MyMatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MyMatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/users/{id}/upcoming",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MyMatches>> getUpcomingContestByUserId(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<MyMatches>> matchesList = myMatchesService.findUpcomingMatchesByUserId(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = MyMatches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MyMatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MyMatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/users/{id}/live",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MyMatches>> getLiveContestByUserId(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<MyMatches>> matchesList = myMatchesService.findLiveMatchesByUserId(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = MyMatches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MyMatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MyMatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/users/{id}/result",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MyMatches>> getResultContestByUserId(@PathVariable @Valid @Pattern(regexp = "[0-9]*")  int id) throws Exception {
        Result<List<MyMatches>> matchesList = myMatchesService.findResultMatchesByUserId(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }
}
