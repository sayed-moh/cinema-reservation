package murraco.service;

import lombok.RequiredArgsConstructor;
import murraco.dto.ReservationsResponse;
import murraco.model.AppUser;
import murraco.model.Movie;
import murraco.model.filmRes;
import murraco.repository.MovieRepository;
import murraco.repository.UserRepository;
import murraco.repository.filmRepository;
import murraco.security.JwtTokenProvider;
import org.apache.catalina.util.ResourceSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MovieService {

    @Autowired
    private MovieRepository movies_Repo;
    @Autowired
    FilesStorageServiceImpl storageService;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final filmRepository filmRepository;
    private final AuthenticationManager authenticationManager;
    @Value("${upload.path}")
    private String uploadPath;
    public ResponseEntity<?> updateMovieData(Long MovieID,Movie movie)
    {
        Optional<Movie> m1 =movies_Repo.findByID(MovieID);
        if(m1.isPresent())
        {
            m1.get().setTitle(movie.getTitle());

            if(m1.get().getScreeningRoom()!=movie.getScreeningRoom())
            {
                //lw et8irt el screening room n3ml reset lel reservation seats
                if(movie.getScreeningRoom()==1)
                {
                    m1.get().setSeats("00000000000000000000");
                    //reservations fe event table n reset it
                    filmRes[] r1=filmRepository.findByMovieID(MovieID);
                    for(int i=0;i<r1.length;i++) {
                        filmRepository.delete(r1[i]);
                    }
                }
                else
                {
                    m1.get().setSeats("000000000000000000000000000000");
                    filmRes[] r1=filmRepository.findByMovieID(MovieID);
                    for(int i=0;i<r1.length;i++) {
                        filmRepository.delete(r1[i]);
                    }
                }
            }

            m1.get().setScreeningRoom(movie.getScreeningRoom());
            m1.get().setMovieDate(movie.getMovieDate());
            m1.get().setStartTime(movie.getStartTime());
            m1.get().setEndTime(movie.getEndTime());
            movies_Repo.save(m1.get());
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            m1.get().setphoto(fileName);
//            String imageURL = m1.get().getId()+" " + m1.get().getScreeningRoom() + ".png";
//            storageService.save(multipartFile,imageURL);
//            m1.get().setUrl("http://localhost:3001/"+ "uploads/" +imageURL);
//            movies_Repo.save(m1.get());
            return new ResponseEntity<>(m1,HttpStatus.OK);

        }
        else
        {
            return new ResponseEntity<>("There is no movie with this ID",HttpStatus.METHOD_NOT_ALLOWED);
        }
    }


    public ResponseEntity<?> updateMovie(Long MovieID,Movie movie,MultipartFile multipartFile)
    {
        Optional<Movie> m1 =movies_Repo.findByID(MovieID);
        if(m1.isPresent())
        {
            String MovieTime=movie.getStartTime();
            String MovieEnd=movie.getEndTime();
            LocalTime LocalTimeMovie=LocalTime.parse(MovieTime);
            LocalTime LocalTimeMovieEnd=LocalTime.parse(MovieEnd);
            int comparetor=LocalTimeMovie.compareTo(LocalTimeMovieEnd);
            if(comparetor>0)
            {
                return new ResponseEntity<>("The end time must be after start time",HttpStatus.METHOD_NOT_ALLOWED);
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            String MovieDate=movie.getMovieDate();
            LocalDate localDateMovie = LocalDate.parse(MovieDate, dtf);
            if(now.isAfter(localDateMovie))
            {
                return new ResponseEntity<>("The date is passed ",HttpStatus.METHOD_NOT_ALLOWED);
            }
            else if(now.isEqual(localDateMovie))
            {
                LocalTime TimeNow=LocalTime.now();
                int value=TimeNow.compareTo(LocalTimeMovie);
                if(value>0)
                {
                    return new ResponseEntity<>("The time is passed ",HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
            m1.get().setTitle(movie.getTitle());
            if(m1.get().getScreeningRoom()!=movie.getScreeningRoom())
            {
                //lw et8irt el screening room n3ml reset lel reservation seats
                if(movie.getScreeningRoom()==1)
                {
                    m1.get().setSeats("00000000000000000000");
                    //reservations fe event table n reset it
                    filmRes[] r1=filmRepository.findByMovieID(MovieID);
                    for(int i=0;i<r1.length;i++) {
                        filmRepository.delete(r1[i]);
                    }
                }
                else
                {
                    m1.get().setSeats("000000000000000000000000000000");
                    filmRes[] r1=filmRepository.findByMovieID(MovieID);
                    for(int i=0;i<r1.length;i++) {
                        filmRepository.delete(r1[i]);
                    }
                }
            }
            m1.get().setScreeningRoom(movie.getScreeningRoom());
            m1.get().setMovieDate(movie.getMovieDate());
            m1.get().setStartTime(movie.getStartTime());
            m1.get().setEndTime(movie.getEndTime());
            movies_Repo.save(m1.get());
            if(multipartFile!=null)
            {
                storageService.delete(m1.get().getphoto());
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                //m1.get().setphoto(fileName);
                String imageURL = m1.get().getId()+"_" + m1.get().getScreeningRoom() + ".png";
                storageService.save(multipartFile,imageURL);
                m1.get().setUrl("http://localhost:3001/Movies/"+ "uploads/" +imageURL);
                movies_Repo.save(m1.get());
            }
            return new ResponseEntity<>(m1,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("There is no movie with this title",HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    public ResponseEntity<?> addMovie2(Movie Movie, MultipartFile multipartFile)
    {
        Movie m1=new Movie();
        String MovieTime=Movie.getStartTime();
        String MovieEnd=Movie.getEndTime();
        LocalTime LocalTimeMovie=LocalTime.parse(MovieTime);
        LocalTime LocalTimeMovieEnd=LocalTime.parse(MovieEnd);
        int comparetor=LocalTimeMovie.compareTo(LocalTimeMovieEnd);
        if(comparetor>0)
        {
            return new ResponseEntity<>("The end time must be after start time",HttpStatus.METHOD_NOT_ALLOWED);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String MovieDate=Movie.getMovieDate();
        LocalDate localDateMovie = LocalDate.parse(MovieDate, dtf);
        if(now.isAfter(localDateMovie))
        {
            return new ResponseEntity<>("The date is passed ",HttpStatus.METHOD_NOT_ALLOWED);
        }
        else if(now.isEqual(localDateMovie))
        {
            LocalTime TimeNow=LocalTime.now();
            int value=TimeNow.compareTo(LocalTimeMovie);
            if(value>0)
            {
                return new ResponseEntity<>("The time is passed ",HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        else;
        m1.setTitle(Movie.getTitle());
        m1.setMovieDate(Movie.getMovieDate());
        m1.setStartTime(Movie.getStartTime());
        m1.setEndTime(Movie.getEndTime());
        m1.setScreeningRoom(Movie.getScreeningRoom());
        if(m1.getScreeningRoom()==1)
        {
            m1.setSeats("00000000000000000000");
        }
        else
        {
            m1.setSeats("000000000000000000000000000000");
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        movies_Repo.save(m1);
        m1.setphoto(m1.getId()+"_" + m1.getScreeningRoom() + ".png");
        String imageURL = m1.getId()+"_" + m1.getScreeningRoom() + ".png";
        storageService.save(multipartFile,imageURL);
        m1.setUrl("http://localhost:3001/Movies/"+ "uploads/" +imageURL);
        movies_Repo.save(m1);
        return new ResponseEntity<>("",HttpStatus.OK);
    }
    public ResponseEntity<?> cancelReservation(Long ResID)
    {
        //check the time


        filmRes r1=filmRepository.findById(ResID);
        String str = r1.getSeats();
        String[] arrOfStr = str.split(",", 50);
        int index=0;
        for(int j=0;j<arrOfStr.length;j++)
        {
            index=Integer.parseInt(arrOfStr[j]);
        }
        LocalDate now = LocalDate.now();
        LocalTime TimeNow=LocalTime.now();
        Optional <Movie> m1=movies_Repo.findByID(r1.getMovie_ID());
        if(m1.isPresent())
        {
            String MovieDate=m1.get().getMovieDate();
            String Time=m1.get().getStartTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String NowString=TimeNow.toString();
            LocalDate localDateMovie = LocalDate.parse(MovieDate, dtf);

            if(now.isEqual(localDateMovie))
            {
                String HourNow1=NowString.substring(0,2);
                String HourMovie1=Time.substring(0,2);
                Integer HourNow=Integer.valueOf(HourNow1);
                Integer HourMovie=Integer.valueOf(HourMovie1);
                if(HourNow-HourMovie<3)
                {
                    return new ResponseEntity<>("the movie will start within 3 hours We can not delete it ",HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
            //
            String Seats=m1.get().getSeats();
            for(int j=0;j<arrOfStr.length;j++)
            {
                index=Integer.parseInt(arrOfStr[j]);
                if(Seats.charAt(index-1)=='1')
                {
                    Seats = Seats.substring(0,index-1)+'0'+Seats.substring(index);
                    m1.get().setSeats(Seats);
                    movies_Repo.save(m1.get());
                }
            }
        }
        filmRepository.delete(r1);
        return new ResponseEntity<>("Reservation with id "+ResID+" has been deleted",HttpStatus.OK);
    }
    public ResponseEntity<?> getMyRes(Integer UserID)
    {
        filmRes[] r1=filmRepository.findByUserID(UserID);
        Set<ReservationsResponse> RR = new HashSet<>();
        ReservationsResponse R1;
        for(int i=0;i<r1.length;i++)
        {

            ReservationsResponse RR1 = new ReservationsResponse();
            RR1.setID(r1[i].getID());
            RR1.setMovie_ID(r1[i].getMovie_ID());
            RR1.setUser_ID(UserID);
            String str = r1[i].getSeats();
            String[] arrOfStr = str.split(",", 50);
            RR1.setSeatsCount(arrOfStr.length);
            Movie movie1=movies_Repo.getById(r1[i].getMovie_ID());
            RR1.setMovieTitle(movie1.getTitle());
            RR1.setMovieDate(movie1.getMovieDate());
            RR.add(RR1);
        }

        return new ResponseEntity<>(RR,HttpStatus.OK);
    }
    ///Reservation
    public ResponseEntity<?> Reservation(Integer[] indices,Long MovieID,String token) {
        //String t=token
        Optional<Movie> targetMovie=movies_Repo.findByID(MovieID);
        String username=jwtTokenProvider.getUsername(token.substring(7));
        AppUser user=userRepository.findByUsername(username);
        int UserID=user.getId();
        if(targetMovie.isPresent())
        {
            for(int i =0;i<indices.length;i++)
            {
                indices[i]=indices[i]+1;
            }
            String x=targetMovie.get().getSeats();
            for(int i=0;i< indices.length;i++)
            {
                if(x.charAt(indices[i]-1)=='1')
                {
                    return new ResponseEntity<>("the Seat with index "+indices[i]+" is reserved",HttpStatus.NOT_FOUND);
                }
            }
            String y= "";
            for(int i=0;i< indices.length;i++)
            {
                x = x.substring(0,indices[i]-1)+'1'+x.substring(indices[i]);
                Integer k = new Integer(indices[i]);
                if(y.length()>0)
                {
                    y = y + "," + k.toString();
                }
                else
                {
                    y = y + k.toString();
                }
            }
            targetMovie.get().setSeats(x);
            movies_Repo.save(targetMovie.get());
            filmRes film=new filmRes();
            film.setMovie_ID(targetMovie.get().getId());
            film.setUser_ID(user.getId());
            film.setSeats(y);
            filmRepository.save(film);
            return new ResponseEntity<>("Reserved",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Movie is not found",HttpStatus.NOT_FOUND);
        }

    }
    public ResponseEntity<?> getAllMovies()
    {
        List<Map<String,String>> dictionaryList = new ArrayList<Map<String,String>>();
        Movie [] arr1=movies_Repo.findAllMovies();
        if(arr1.length==0)
        {
            return new ResponseEntity<>("There is no available movies", HttpStatus.NOT_FOUND);
        }
        else {
            for (int i = 0; i < arr1.length; i++) {
                Optional<Movie> movie = movies_Repo.findByID(arr1[i].getId());
                Integer countSpecial = 0;
                if (movie.isPresent()) {
                    String countSeats = movie.get().getSeats();
                    countSpecial = 0;
                    for (int j = 0; j < countSeats.length(); j++) {

                        if (countSeats.substring(j, j + 1).matches("0")) {
                            countSpecial++;
                        }
                    }
                }
                String title = movie.get().getTitle();
                String date = movie.get().getMovieDate();
                String startTime = movie.get().getStartTime();
                String endTime = movie.get().getEndTime();
                Integer screeningRoom = movie.get().getScreeningRoom();
                String seats = movie.get().getSeats();
                String Url = movie.get().getUrl();
                Hashtable<String, String> my_dict = new Hashtable<String, String>();
                my_dict.put("title", title);
                my_dict.put("movieDate", date);
                my_dict.put("startTime", startTime);
                my_dict.put("endTime", endTime);
                my_dict.put("screeningRoom", screeningRoom.toString());
                my_dict.put("url", Url);
                my_dict.put("vacantSeats", countSpecial.toString());
                my_dict.put("seats", seats);
                my_dict.put("id",movie.get().getId().toString());

                dictionaryList.add(my_dict);
            }
            return new ResponseEntity<>(dictionaryList, HttpStatus.OK);
        }
    }
    public ResponseEntity<?> getVacantSeats(Long MovieID)
    {
        Optional<Movie> movie=movies_Repo.findByID(MovieID);
        if(movie.isPresent())
        {
            String countSeats=movie.get().getSeats();
            int countSpecial=0;
            for (int i = 0; i < countSeats.length(); i++) {
                if (countSeats.substring(i, i + 1).matches("0")) {
                    countSpecial++;
                }
            }
            return new ResponseEntity<>(countSpecial, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("There is no movie has this ID" , HttpStatus.NOT_FOUND);
        }

    }
    public ResponseEntity<?> deleteMovie(Long MovieID)
    {
        Optional<Movie>movie= Optional.of(movies_Repo.getById(MovieID));
        if(movie.isPresent())
        {
        filmRes[]filmRes=filmRepository.findByMovieID(MovieID);
        for(int i=0;i<filmRes.length;i++)
        {
            filmRepository.delete(filmRes[i]);
        }
        movies_Repo.delete(movie.get());
        return new ResponseEntity<>("Deleted" , HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("There is no movie with this ID" , HttpStatus.NOT_FOUND);

        }
    }

}
