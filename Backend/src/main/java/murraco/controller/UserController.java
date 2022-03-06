package murraco.controller;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import murraco.model.AppUser;
import murraco.service.FilesStorageServiceImpl;
import murraco.service.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import murraco.dto.UserDataDTO;
import murraco.dto.UserResponseDTO;
import murraco.service.UserService;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
  private final UserService userService;
  private final ModelMapper modelMapper;
  private final MovieService movieService;
  @Autowired
  FilesStorageServiceImpl fileService;
//
//  @GetMapping("/Movie/Get")
//  @ApiOperation(value = "${MovieController.getAll}")
//  @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_MANAGER') or hasRole('ROLE_CLIENT')")
//  public ResponseEntity<?> getAllMovie() {
//    return movieService.getAllMovies();
//  }

  @DeleteMapping ("/deleteUser")
  @ApiOperation(value = "${UserController.DeleteUser}")
  @PreAuthorize("hasRole('ROLE_ADMIN') ")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 422, message = "Username is already in use")})
  public HttpEntity<?> DeleteUser(@ApiParam("Username") @RequestParam String username)
  {
    return userService.deleteUser(username);
  }

  @PutMapping("/ApproveUser/update")
  @ApiOperation(value = "${UserController.getApproveUserUpdate}")
  @PreAuthorize("hasRole('ROLE_ADMIN') ")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 422, message = "Username is already in use")})
  public HttpEntity<?> updateApproved(@RequestHeader("Authorization") String token,@ApiParam("Username") @RequestParam String username)
  {
    return userService.updateApproved(username);
  }

  @GetMapping("/ApproveUser")
  @ApiOperation(value = "${UserController.getApproveUser}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiResponses(value = {//
          @ApiResponse(code = 400, message = "Something went wrong"), //
          @ApiResponse(code = 403, message = "Access denied"), //
          @ApiResponse(code = 422, message = "Username is already in use")})
  @CrossOrigin(origins = "http://localhost:3000")
  public HttpEntity<?> getUserNotApproved()
  {
    return userService.getUserNotApproved();
  }

  @PostMapping("/signin")
  @ApiOperation(value = "${UserController.signin}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 422, message = "Invalid username/password supplied")})
  public ResponseEntity<?> login(//
      @ApiParam("Username") @RequestParam String username, //
      @ApiParam("Password") @RequestParam String password) {
    return userService.signin(username, password);
  }

  @PostMapping("/signup")
  @ApiOperation(value = "${UserController.signup}")
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 422, message = "Username is already in use")})
  public ResponseEntity<?> signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
    return userService.signup(modelMapper.map(user, AppUser.class));
  }

  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }
//
//  @GetMapping(value = "/{username}")
//  @PreAuthorize("hasRole('ROLE_ADMIN')")
//  @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
//  @ApiResponses(value = {//
//      @ApiResponse(code = 400, message = "Something went wrong"), //
//      @ApiResponse(code = 403, message = "Access denied"), //
//      @ApiResponse(code = 404, message = "The user doesn't exist"), //
//      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
//  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
//    return modelMapper.map(userService.search(username), UserResponseDTO.class);
//  }
//
//  @GetMapping(value = "/me")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//  @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
//  @ApiResponses(value = {//
//      @ApiResponse(code = 400, message = "Something went wrong"), //
//      @ApiResponse(code = 403, message = "Access denied"), //
//      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
//  public UserResponseDTO whoami(HttpServletRequest req) {
//    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
//  }
//
//  @GetMapping("/refresh")
//  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//  public String refresh(HttpServletRequest req) {
//    return userService.refresh(req.getRemoteUser());
//  }

}
