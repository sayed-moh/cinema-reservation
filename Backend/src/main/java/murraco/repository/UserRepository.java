package murraco.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import murraco.model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

  boolean existsByUsername(String username);

  AppUser findByUsername(String username);

  Optional<AppUser> findUserByUsername(String username);

  @Transactional
  void deleteByUsername(String username);


  //public  AppUser findUserByUsername(@Param("userName")String userName);

  @Query(value = "Select user_name From bked.user" ,nativeQuery = true)
  public String[] findAllUsers();

  @Query(value = "Select email From bked.user" ,nativeQuery = true)
  public String[] findAllEmails();

  @Query(value = "select * from testbked.app_user as E where approved=0 and E.id in ( SELECT app_user_id from testbked.app_user_app_user_roles as R where ( R.app_user_roles=2));" ,nativeQuery = true)
  public Set<AppUser> findUserNotApproved();


}
