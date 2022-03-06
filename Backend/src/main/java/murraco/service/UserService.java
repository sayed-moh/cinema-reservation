package murraco.service;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import murraco.model.AppUserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import murraco.exception.CustomException;
import murraco.model.AppUser;
import murraco.repository.UserRepository;
import murraco.security.JwtTokenProvider;

import java.util.*;

import static murraco.model.AppUserRole.ROLE_MANAGER;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;

  public ResponseEntity<?> signin(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      String token = jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
      AppUser user = userRepository.findByUsername(username);
      String role = user.getAppUserRoles().toString();
      if (user.getAppUserRoles().contains(ROLE_MANAGER) && user.getApproved()==0){
        role = "[ROLE_CLIENT]";
      }
      Hashtable<String, String> my_dict = new Hashtable<String, String>();
      my_dict.put("token",token);
      my_dict.put("role",role);
      return new ResponseEntity<>(my_dict,HttpStatus.OK);
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
  public ResponseEntity<?> deleteUser(String userName)
  {
    Optional<AppUser> user=userRepository.findUserByUsername(userName);

    if(!user.isPresent())
    {
      return new ResponseEntity<>(userName+" Not Found",HttpStatus.NOT_FOUND);
    }
    else
    {
      userRepository.deleteById(user.get().getId());
      return new ResponseEntity<>(userName+" has been deleted",HttpStatus.OK);
    }
  }
  public ResponseEntity<?> updateApproved(String userName)
  {
    AppUser user=userRepository.findByUsername(userName);
    user.setApproved(1);
    userRepository.save(user);
    return new ResponseEntity<>("Done",HttpStatus.OK);
  }

  public ResponseEntity<?> getUserNotApproved()
  {
    Set<AppUser> managerNotApproved = userRepository.findUserNotApproved();
    if(!managerNotApproved.isEmpty()) {
      //ArrayList<Tuple> body=new ArrayList<Tuple>();
      //Hashtable<String, String> my_dict = new Hashtable<String, String>();
      List<Map<String,String>> dictionaryList = new ArrayList<Map<String,String>>();
      AppUser[] m2=managerNotApproved.toArray(new AppUser[0]);
      System.out.println(m2);
      for(int i=0;i<m2.length;i++)
      {
        String userName = m2[i].getUsername();
        String Email = m2[i].getEmail();
        String firstName = m2[i].getFirstName();
        String lastName = m2[i].getLastName();
        Hashtable<String, String> my_dict = new Hashtable<String, String>();
        my_dict.put("userName",userName);
        my_dict.put("email",Email);
        my_dict.put("firstName",firstName);
        my_dict.put("lastName",lastName);
        dictionaryList.add(my_dict);

      }
      return new ResponseEntity<>(dictionaryList,HttpStatus.OK);
    }
    else
    {
      return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
    }
  }
  public ResponseEntity<?> signup(AppUser appUser) {
    if (!userRepository.existsByUsername(appUser.getUsername())) {
      appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
      userRepository.save(appUser);
      String token = jwtTokenProvider.createToken(appUser.getUsername(), userRepository.findByUsername(appUser.getUsername()).getAppUserRoles());
      //AppUser user = userRepository.findByUsername(appUser.getUsername());
      String role = appUser.getAppUserRoles().toString();
      if(appUser.getAppUserRoles().contains(ROLE_MANAGER)){
        role = "[ROLE_CLIENT]";
      }
      Hashtable<String, String> my_dict = new Hashtable<String, String>();
      my_dict.put("token",token);
      my_dict.put("role",role);
      return new ResponseEntity<>(my_dict,HttpStatus.OK);
    } else {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String username) {
    userRepository.deleteByUsername(username);
  }

  public AppUser search(String username) {
    AppUser appUser = userRepository.findByUsername(username);
    if (appUser == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return appUser;
  }

  public AppUser whoami(HttpServletRequest req) {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
  }

  public String refresh(String username) {
    return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
  }

}
