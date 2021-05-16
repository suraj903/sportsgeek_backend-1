package com.project.sportsgeek.controller;

import com.project.sportsgeek.exception.MatchesException;
import com.project.sportsgeek.model.Matches;
import com.project.sportsgeek.model.MatchesWithVenue;
import com.project.sportsgeek.model.Venue;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.MatchesService;
import io.swagger.annotations.ApiParam;
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
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/matches",produces = MediaType.APPLICATION_JSON_VALUE)
public class MatchesController {

    @Autowired
    MatchesService matchesService;

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchesWithVenue>> getAllMatches() throws Exception {
        Result<List<MatchesWithVenue>> matchesList = matchesService.findAllMatches();
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MatchesWithVenue> getMatchesById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<MatchesWithVenue> matchesList = matchesService.findMatchesById(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/tournament/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchesWithVenue>> getMatchesByTournament(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<MatchesWithVenue>> matchesList = matchesService.findMatchesByTournament(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/venue/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchesWithVenue>> getMatchesByVenue(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<MatchesWithVenue>> matchesList = matchesService.findMatchesByVenue(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/minimum-bet/{minPoints}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchesWithVenue>> getMatchesByMinBet(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int minPoints) throws Exception {
        Result<List<MatchesWithVenue>> matchesList = matchesService.findMatchesByMinPoints(minPoints);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/team/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchesWithVenue>> getMatchesByTeam(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<MatchesWithVenue>> matchesList = matchesService.findMatchesByTeam(id);
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/old-match",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MatchesWithVenue>> findAllMatchesByPreviousDateAndResultStatus() throws Exception {
        Result<List<MatchesWithVenue>> matchesList = matchesService.findAllMatchesByPreviousDateAndResultStatus();
        return new ResponseEntity<>(matchesList.getData(), HttpStatus.valueOf(matchesList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/update-match/{matchId}/{resultStatus}/{winnerTeamId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matches> updateMatchResult(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int matchId,@PathVariable @Valid @Pattern(regexp = "[0-9]*") int resultStatus, @PathVariable @Valid @Pattern(regexp = "[0-9]*") int winnerTeamId) throws Exception {
        Result<String> updateResult = matchesService.updateMatchWinningTeam(matchId,resultStatus,winnerTeamId);
        return new ResponseEntity(updateResult.getData(),HttpStatus.valueOf(updateResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{id}/update-match-detail",produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Matches> updateMatch(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@ApiParam(value = "tournamentId") @RequestParam("tournamentId") int tournamentId, @ApiParam(value = "name") @RequestParam("name") String name,@ApiParam(value = "startDateTime") @RequestParam("startDateTime") Timestamp startDateTime,@ApiParam(value = "venueId") @RequestParam("venueId") int venueId,@ApiParam(value = "team1") @RequestParam("team1") int team1,@ApiParam(value = "team2") @RequestParam("team2") int team2,@ApiParam(value = "winnerTeamId") @RequestParam("winnerTeamId") int winnerTeamId,@ApiParam(value = "resultStatus") @RequestParam("resultStatus") boolean resultStatus,@ApiParam(value = "minimumBet") @RequestParam("minimumBet") int minimumBet) throws Exception {
//        Matches  matches = Matches.builder()
//                .tournamentId(tournamentId)
//                .name(name)
//                .startDateTime(startDateTime)
//                .venueId(venueId)
//                .team1(team1)
//                .team2(team2)
//                .winnerTeamId(winnerTeamId)
//                .resultStatus(resultStatus)
//                .minimumBet(minimumBet).build();
//        Result<Matches> matchResult = matchesService.updateMatch(id, matches);
//        return new ResponseEntity(matchResult.getData(), HttpStatus.valueOf(matchResult.getCode()));
//    }
    public ResponseEntity<Matches> addMatches(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id, @RequestBody(required = true) @Valid Matches matches) throws  Exception {
        Result<Matches> matchResult = matchesService.updateMatch(id, matches);
        return new ResponseEntity(matchResult.getData(), HttpStatus.valueOf(matchResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{id}/update-match-venue/{venueId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMatchVenue(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@PathVariable @Valid @Pattern(regexp = "[0-9]*") int venueId ) throws Exception {
        Result<String> matchResult = matchesService.updateMatchVenue(id, venueId);
        return new ResponseEntity(matchResult.getData(),HttpStatus.valueOf(matchResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{id}/update-match-result-status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMatchResultStatus(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@PathVariable @Valid @Pattern(regexp = "[0-9]*") boolean status ) throws Exception {
        Result<String> matchResult = matchesService.updateMatchResultStatus(id, status);
        return new ResponseEntity(matchResult.getData(),HttpStatus.valueOf(matchResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{id}/update-match-start-date/{date}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMatchStartDateTime(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id, @PathVariable @Valid Timestamp date) throws Exception {
        Result<String> matchResult = matchesService.updateMatchStartDateTime(id, date);
        return new ResponseEntity(matchResult.getData(), HttpStatus.valueOf(matchResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{matchId}/update-min-bet/{minBet}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateMinimumBet(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int matchId, @PathVariable @Valid @Pattern(regexp = "[0-9]*") int minBet) throws  Exception {
        Result<String> userResult = matchesService.updateMinimumBet(matchId,minBet);
        return new ResponseEntity(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Matches.class),
                    @ApiResponse(code = 400, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Matches> addMatches(@ApiParam(value = "matchId") @RequestParam("matchId") int matchId,@ApiParam(value = "tournamentId") @RequestParam("tournamentId") int tournamentId, @ApiParam(value = "name") @RequestParam("name") String name,@ApiParam(value = "startDateTime") @RequestParam("startDateTime") Timestamp startDateTime,@ApiParam(value = "venueId") @RequestParam("venueId") int venueId,@ApiParam(value = "team1") @RequestParam("team1") int team1,@ApiParam(value = "team2") @RequestParam("team2") int team2,@ApiParam(value = "minimumBet") @RequestParam("minimumBet") int minimumBet) throws  Exception {
//        Matches matches = Matches.builder()
//                .matchId(matchId)
//                .tournamentId(tournamentId)
//                .name(name)
//                .startDateTime(startDateTime)
//                .venueId(venueId)
//                .team1(team1)
//                .team2(team2)
//                .minimumBet(minimumBet).build();
//        Result<Matches> matchesResult = matchesService.addMatches(matches);
//        return new ResponseEntity(matchesResult.getData(), HttpStatus.valueOf(matchesResult.getCode()));
//    }
    public ResponseEntity<Matches> addMatches(@RequestBody(required = true) @Valid Matches matches) throws  Exception {
        Result<Matches> matchesResult = matchesService.addMatches(matches);
        return new ResponseEntity(matchesResult.getData(), HttpStatus.valueOf(matchesResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Matches.class),
                    @ApiResponse(code = 404, message = "Bad request", response = MatchesException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = MatchesException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Matches> deleteMatchById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<Integer> integerResult = matchesService.deleteMatch(id);
        return new ResponseEntity(integerResult.getData(), HttpStatus.valueOf(integerResult.getCode()));
    }
}
