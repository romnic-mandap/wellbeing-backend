package com.example.meal2.thoughtrecord.controller;

import com.example.meal2.aftermealnote.dto.AfterMealNoteDetailedDTO;
import com.example.meal2.aftermealnote.dto.AfterMealNoteUpdateDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtRecordCreationDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtRecordDTO;
import com.example.meal2.thoughtrecord.dto.ThoughtRecordUpdateDTO;
import com.example.meal2.thoughtrecord.service.ThoughtRecordService;
import com.example.meal2.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Tag(
        name="ThoughtRecord controller",
        description = "provides api for ThoughtRecord"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1")
public class ThoughtRecordController {

    private final String PATH_HEADER = "/thought-records";
    private final ThoughtRecordService thoughtRecordService;

    public ThoughtRecordController(ThoughtRecordService thoughtRecordService) {
        this.thoughtRecordService = thoughtRecordService;
    }

    @Operation(
            summary="add new ThoughtRecord object",
            description="add new ThoughtRecord object"
    )
    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="ThoughtRecordDTO",
                    content={@Content(
                            mediaType="application/json",
                            schema=@Schema(implementation= ThoughtRecordDTO.class)
                    )}
            )
    })
    @PostMapping(value=PATH_HEADER, consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> addThoughtRecord(
            @AuthenticationPrincipal User user,
            @RequestBody ThoughtRecordCreationDTO thoughtRecordCreationDTO
            ){
        return new ResponseEntity<>(
                thoughtRecordService.createThoughtRecord(user, thoughtRecordCreationDTO),
                HttpStatus.CREATED
        );
    }

    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="ThoughtRecordDTO",
                    content={@Content(
                            mediaType="application/json",
                            schema=@Schema(implementation= ThoughtRecordDTO.class)
                    )}
            )
    })
    @GetMapping(value=PATH_HEADER+"/{id}", produces={"application/json"})
    public ResponseEntity<?> getThoughtRecord(
            @AuthenticationPrincipal User user,
            @Parameter(description="ThoughtRecord id")
            @PathVariable("id") Long id
    ){
        return new ResponseEntity<>(
                thoughtRecordService.getThoughtRecord(user, id),
                HttpStatus.OK
        );
    }

    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="list of ThoughtRecordDTOs",
                    content={@Content(
                            mediaType="application/json",
                            array=@ArraySchema(schema=@Schema(implementation=ThoughtRecordDTO.class))
                    )}
            )
    })
    @GetMapping(value=PATH_HEADER, produces={"application/json"})
    public ResponseEntity<?> getAllThoughtRecords(
            @AuthenticationPrincipal User user,

            @Parameter(description="search situation", schema=@Schema(type="string"))

            @RequestParam Optional<String> q,

            @Parameter(description="page (starts at 0)")
            @RequestParam Optional<Integer> p,

            @Parameter(description="size (default: 32, max: 50)")
            @RequestParam Optional<Integer> s,

            @Parameter(description="start date (yyyy-mm-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> sd,

            @Parameter(description="end date (yyyy-mm-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ed,

            @Parameter(description="start time (hh:mm:ss)", schema=@Schema(type="string", format="time"))
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> st,

            @Parameter(description="end time (hh:mm:ss)", schema=@Schema(type="string", format="time"))
            @RequestParam @DateTimeFormat(pattern="hh:mm:ss") Optional<LocalTime> et
    ){
        String search = q.orElse("");
        Integer page = p.orElse(0);
        Integer size = s.orElse(0);
        LocalDate startDate = sd.orElse(null);
        LocalDate endDate = ed.orElse(null);
        LocalTime startTime = st.orElse(null);
        LocalTime endTime = et.orElse(null);

        return new ResponseEntity<>(
                thoughtRecordService.getThoughtRecords(user, search, page, size, startDate, endDate, startTime, endTime),
                HttpStatus.OK
        );
    }

    @ApiResponses(value={
            @ApiResponse(
                    responseCode="200",
                    description="ThoughtRecordDTO",
                    content={@Content(
                            mediaType="application/json",
                            schema=@Schema(implementation= ThoughtRecordDTO.class)
                    )}
            )
    })
    @PutMapping(value=PATH_HEADER+"/{id}", consumes={"application/json"}, produces={"application/json"})
    public ResponseEntity<?> updateThoughtRecord(
            @AuthenticationPrincipal User user,
            @Parameter(description="ThoughtRecord id")
            @PathVariable("id") Long id,
            @RequestBody ThoughtRecordUpdateDTO thoughtRecordUpdateDTO)
    {
        return new ResponseEntity<>(
                thoughtRecordService.updateThoughtRecord(user, id, thoughtRecordUpdateDTO),
                HttpStatus.OK
        );
    }

    @Operation(
            summary="delete ThoughtRecord object",
            description="delete ThoughtRecord object that matches id and has proper owner"
    )
    @DeleteMapping(value=PATH_HEADER+"/{id}", produces={"application/json"})
    public ResponseEntity<?> deleteThoughtRecord(
            @AuthenticationPrincipal User user,
            @Parameter(description="ThoughtRecord id")
            @PathVariable("id") Long id){
        thoughtRecordService.deleteThoughtRecord(user, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value=PATH_HEADER+"/month-mood-scores", produces={"application/json"})
    public ResponseEntity<?> getMonthMoodScores(
            @AuthenticationPrincipal User user,
            @RequestParam(required=true) String d  // yyyy-mm
    ){
        return new ResponseEntity<>(
                thoughtRecordService.getMonthMoodScores(user, d),
                HttpStatus.OK
        );
    }
}
