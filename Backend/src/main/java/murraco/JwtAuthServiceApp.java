package murraco;

import lombok.RequiredArgsConstructor;
import murraco.model.AppUser;
import murraco.model.AppUserRole;

import murraco.repository.UserRepository;
import murraco.repository.filmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import murraco.service.UserService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
@RequiredArgsConstructor
public class JwtAuthServiceApp implements CommandLineRunner {

  final UserService userService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  filmRepository filmRepository;
  //  final SeatRepository seatRepository;
  public static void main(String[] args) {
    SpringApplication.run(JwtAuthServiceApp.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
//  public static boolean isSameHour(LocalDateTime timestamp,
//                                   LocalDateTime timestampToCompare) {
//    return timestamp.truncatedTo(HOURS)
//            .isEqual(timestampToCompare.truncatedTo(HOURS));
//  }
//
//  public static boolean isSameDay(Date date, Date dateToCompare) {
//    return DateUtils.isSameDay(date, dateToCompare);
//  }
//
//  public static boolean isSameHour(Date date, Date dateToCompare) {
//    return DateUtils.truncatedEquals(date, dateToCompare, Calendar.HOUR);
//  }

  @Override
  public void run(String... params) throws Exception {

    if (userRepository.count() == 0) {
      AppUser admin = new AppUser();
      admin.setUsername("admin");
      admin.setPassword("admin");
      admin.setEmail("admin@email.com");
      admin.setFirstName("Ahmed");
      admin.setLastName("Gaber");
      admin.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_ADMIN)));

      userService.signup(admin);

      AppUser client = new AppUser();
      client.setUsername("client");
      client.setPassword("client");
      client.setEmail("client@email.com");
      client.setFirstName("Mohamed");
      client.setLastName("Elsayed");
      client.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_CLIENT)));
      userService.signup(client);

      AppUser Manager = new AppUser();
      Manager.setUsername("manager");
      Manager.setPassword("manager");
      Manager.setEmail("manager@email.com");
      Manager.setFirstName("Ibrahim");
      Manager.setLastName("wael");
      Manager.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_MANAGER)));

      userService.signup(Manager);

//      AppUser Guest = new AppUser();
//      Guest.setUsername("Guest");
//      Guest.setPassword("Guest");
//      Guest.setEmail("Guest@email.com");
//      Guest.setFirstName("Ahmedz");
//      Guest.setLastName("Osama");
//      Guest.setAppUserRoles(new ArrayList<AppUserRole>(Arrays.asList(AppUserRole.ROLE_GUEST)));
//      userService.signup(Guest);


      //LocalDate secondDate = LocalDate.of(2019, 7, 1);
    }
//    LocalDate currentDate = LocalDate.of(2020, 1, 1);
//    LocalTime time1 = LocalTime.parse("18:00");
//    Movie[] movies= movieRepository.findAllMovies();
//    for(int i=0;i< movies.length;i++)
//    {
//
//      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
//      String MovieDate=movies[i].getMovieDate();
//
//      LocalTime movieEndTime = LocalTime.parse(movies[i].getEndTime());
//      LocalDate localDateMovie = LocalDate.parse(MovieDate, formatter);
//      if(localDateMovie.isBefore(currentDate))
//      {
//        Long ID =movies[i].getId();
//        filmRes[] res =filmRepository.findByMovieID(ID);
//        //delete el reservations
//        for(int j=0;j< res.length;j++) {
//          filmRepository.delete(res[j]);
//        }
//        movieRepository.delete(movies[i]);
//      }
//      else if (localDateMovie.isEqual(currentDate))
//      {
//        //checking time
//        int value = time1.compareTo(movieEndTime);
//        if(value>0)
//        {
//          Long ID =movies[i].getId();
//          filmRes[] res =filmRepository.findByMovieID(ID);
//          //delete el reservations
//          for(int j=0;j< res.length;j++) {
//            filmRepository.delete(res[j]);
//          }
//          movieRepository.delete(movies[i]);
//        }
//
//      }
//
//    }
  }
}
