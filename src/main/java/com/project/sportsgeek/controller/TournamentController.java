package com.project.sportsgeek.controller;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.exception.TournamentException;
import com.project.sportsgeek.model.Tournament;
import com.project.sportsgeek.model.Venue;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.TournamentService;
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
@RequestMapping(path = "/tournaments",produces = MediaType.APPLICATION_JSON_VALUE)
public class TournamentController {

    @Autowired
    TournamentService tournamentService;

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Venue.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = TournamentException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tournament>> getAllTournament() {
        Result<List<Tournament>> tournamentList = tournamentService.findAllTournament();
        return new ResponseEntity<>(tournamentList.getData(), HttpStatus.valueOf(tournamentList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Venue.class),
                    @ApiResponse(code = 404, message = "Bad request", response = ResultException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = TournamentException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tournament> getTournamentById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception{
        Result<Tournament> tournamentList = tournamentService.findTournamentById(id);
        return new ResponseEntity<>(tournamentList.getData(), HttpStatus.valueOf(tournamentList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Venue.class),
                    @ApiResponse(code = 400, message = "Bad request", response = TournamentException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = TournamentException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tournament> addTournament(@RequestBody(required = true) @Valid Tournament Tournament) throws  Exception {
        Result<Tournament> tournamentResult = tournamentService.addTournament(Tournament);
        return new ResponseEntity(tournamentResult.getData(), HttpStatus.valueOf(tournamentResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Venue.class),
                    @ApiResponse(code = 400, message = "Bad request", response = TournamentException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = TournamentException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tournament> updateTournament(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@RequestBody(required = true) @Valid Tournament Tournament) throws Exception {
        Result<Tournament> tournamentResult = tournamentService.updateTournament(id,Tournament);
        return new ResponseEntity(tournamentResult.getData(), HttpStatus.valueOf(tournamentResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Venue.class),
                    @ApiResponse(code = 400, message = "Bad request", response = TournamentException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = TournamentException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/activate-tournament/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateActiveTournament(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<String> tournamentResult = tournamentService.updateActiveTournament(id);
        return new ResponseEntity(tournamentResult.getData(), HttpStatus.valueOf(tournamentResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Venue.class),
                    @ApiResponse(code = 404, message = "Bad request", response = TournamentException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = TournamentException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<Tournament>> deleteTournamentById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception{
        Result<Integer> tournamentResult = tournamentService.deleteTournament(id);
        return new ResponseEntity(tournamentResult,HttpStatus.valueOf(tournamentResult.getCode()));
    }
}
