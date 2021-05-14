package com.project.sportsgeek.controller;

import com.project.sportsgeek.exception.PlayerException;
import com.project.sportsgeek.model.Player;
import com.project.sportsgeek.model.PlayerResponse;
import com.project.sportsgeek.model.Player;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.PlayerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping(path = "/players",produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Player.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlayerResponse>> getAllPlayer() {
        Result<List<PlayerResponse>> playerList = playerService.findAllPlayer();
        return new ResponseEntity<>(playerList.getData(), HttpStatus.valueOf(playerList.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Player.class),
                    @ApiResponse(code = 404, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<PlayerResponse> playerResult = playerService.findPlayerById(id);
        return new ResponseEntity<>(playerResult.getData(), HttpStatus.valueOf(playerResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Player.class),
                    @ApiResponse(code = 404, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/player-type/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlayerResponse>> getPlayerByPlayerType(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<PlayerResponse>> playerResult = playerService.findPlayerByPlayerType(id);
        return new ResponseEntity<>(playerResult.getData(), HttpStatus.valueOf(playerResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Player.class),
                    @ApiResponse(code = 404, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping(value = "/team/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlayerResponse>> getPlayerByTeam(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<List<PlayerResponse>> playerResult = playerService.findPlayerByTeamId(id);
        return new ResponseEntity<>(playerResult.getData(), HttpStatus.valueOf(playerResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Player.class),
                    @ApiResponse(code = 400, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
//    @RequestBody(required = true) @Valid Player player
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> addPlayer(@RequestParam("playerId") int playerId,@RequestParam("teamId") int teamId,@RequestParam("name") String name,@RequestParam("typeId") int typeId,@RequestParam("profilePicture") MultipartFile multipartFile,@RequestParam("credits") Double credits ) throws Exception {
       String filename = multipartFile.getOriginalFilename();
       Player player = Player.builder()
               .playerId(playerId)
               .teamId(teamId)
               .name(name)
               .typeId(typeId)
               .profilePicture(filename)
               .credits(credits).build();
        Result<Player> playerResult = playerService.addPlayer(player,multipartFile);
        return new ResponseEntity(playerResult.getData(),HttpStatus.valueOf(playerResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Player.class),
                    @ApiResponse(code = 400, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
//    @RequestBody(required = true) @Valid Player player
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> updatePlayer(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id,@RequestParam("teamId") int teamId,@RequestParam("name") String name,@RequestParam("typeId") int typeId,@RequestParam("profilePicture") MultipartFile multipartFile,@RequestParam("credits") Double credits) throws Exception {
        String filename = multipartFile.getOriginalFilename();
        Player player = Player.builder()
                .teamId(teamId)
                .name(name)
                .typeId(typeId)
                .profilePicture(filename)
                .credits(credits).build();
        Result<Player> playerResult = playerService.updatePlayer(id, player,multipartFile);
        return new ResponseEntity(playerResult.getData(),HttpStatus.valueOf(playerResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 201, message = "success", response = Player.class),
                    @ApiResponse(code = 400, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @PutMapping(value = "/{id}/update-player-type/{typeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePlayerType(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id, @PathVariable @Valid @Pattern(regexp = "[0-9]*") int typeId) throws Exception {
        Result<String> playerResult = playerService.updatePlayerType(id, typeId);
        return new ResponseEntity(playerResult.getData(),HttpStatus.valueOf(playerResult.getCode()));
    }

    @ApiResponses(value =
            {
                    @ApiResponse(code = 200, message = "success", response = Player.class),
                    @ApiResponse(code = 404, message = "Bad request", response = PlayerException.class),
                    @ApiResponse(code = 500, message = "Unfortunately there is technical error while processing your request", response = PlayerException.class),
                    @ApiResponse(code = 403 , message = "Forbidden!! Access is Denied!")
            }
    )
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<Player>> deletePlayerById(@PathVariable @Valid @Pattern(regexp = "[0-9]*") int id) throws Exception {
        Result<Integer> integerResult =  playerService.deletePlayer(id);
        return new ResponseEntity(integerResult,HttpStatus.valueOf(integerResult.getCode()));
    }
}
