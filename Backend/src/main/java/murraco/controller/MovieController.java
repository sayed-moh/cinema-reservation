package murraco.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import murraco.model.AppUser;
import murraco.model.Movie;
import murraco.repository.UserRepository;
import murraco.security.JwtTokenProvider;
import murraco.service.FilesStorageServiceImpl;
import murraco.service.MovieService;
import murraco.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/Movies")
@Api(tags = "movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieService movieService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    FilesStorageServiceImpl fileService;

    @GetMapping("/VacantSeats")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_MANAGER') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> getVacantSeats(@RequestParam(name = "movie") Long MovieID)
    {
        return movieService.getVacantSeats(MovieID);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/Movie/Get")
    public ResponseEntity<?> getAllMovie() {
        return movieService.getAllMovies();
    }

    @PutMapping("/Movie/Update")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateMovie(@RequestHeader("Authorization") String token,
                                         @RequestPart("movie") Movie movie,
                                         @RequestPart(value = "poster", required = false) MultipartFile file) {
        String username=jwtTokenProvider.getUsername(token.substring(7));
        AppUser user=userRepository.findByUsername(username);
        if(user.getApproved()==1)
        {
            //Long m=new Long(MovieID);
            return movieService.updateMovie(movie.getId(),movie,file);
        }
        else
        {
            return new ResponseEntity<>("The manager is not approved", HttpStatus.OK);
        }
    }
    @PutMapping("/Movie/Update/DataOnly")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> updateMovieData(@RequestHeader("Authorization") String token,
                                         @RequestPart("movie") Movie movie) {
        String username=jwtTokenProvider.getUsername(token.substring(7));
        AppUser user=userRepository.findByUsername(username);
        if(user.getApproved()==1)
        {
            //Long m=new Long(MovieID);
            return movieService.updateMovieData(movie.getId(),movie);
        }
        else
        {
            return new ResponseEntity<>("The manager is not approved", HttpStatus.OK);
        }
    }

    @PostMapping("/Movie/Add")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> addMovie(@RequestHeader("Authorization") String token,
                                      @RequestPart("movie") Movie movie,
                                      @RequestPart("poster") MultipartFile file) {
        String username=jwtTokenProvider.getUsername(token.substring(7));
        AppUser user=userRepository.findByUsername(username);
        if(user.getApproved()==1) {
            return movieService.addMovie2(movie, file);
        }
        else
        {return new ResponseEntity<>("The manager is not approved", HttpStatus.OK); }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/uploads/{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getImageAsByteArray(@PathVariable String filename, HttpServletResponse response) throws IOException {
        Resource file = fileService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    ///////////////////////////////////Reservation
    @PostMapping("/Reservation")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> Reservation(@RequestHeader("Authorization") String token,
                                         @RequestBody Integer[] indices,
                                         @RequestParam("Movie")int MovieID) {
        Long id=Long.valueOf(MovieID);
        //int[] index
        System.out.println(id);
        return movieService.Reservation(indices,id,token);
    }
    @GetMapping("/Get/Reservations")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> getReservations(@RequestHeader("Authorization") String token)
    {
        String username=jwtTokenProvider.getUsername(token.substring(7));
        AppUser user=userRepository.findByUsername(username);
        return movieService.getMyRes(user.getId());
    }

    @DeleteMapping("/Delete/Reservations")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> DeleteReservations(@RequestParam("Res")Long ResID)
    {
        return movieService.cancelReservation(ResID);
    }
    @DeleteMapping("/Movie/Delete")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<?> deleteMovie(@RequestHeader("Authorization") String token,
                                      @RequestParam("movie")Long MovieID) {
        String username=jwtTokenProvider.getUsername(token.substring(7));
        AppUser user=userRepository.findByUsername(username);
        if(user.getApproved()==1) {
           return movieService.deleteMovie(MovieID);
        }
        else
        {return new ResponseEntity<>("The manager is not approved", HttpStatus.OK); }
    }



}
