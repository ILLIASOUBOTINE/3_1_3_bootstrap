package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

@Service
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {

   private final UserDao userDao;

   @Autowired
   public UserServiceImp(UserDao userDao) {
      this.userDao = userDao;
   }

   @Override
   public void add(User user) {
      userDao.add(user);
   }

   @Override
   public List<User> listUsers() {
      return userDao.listUsers();
   }

   @Override
   public void update(User user) {
      userDao.update(user);
   }

   @Override
   public void delete(Long id) {
      userDao.delete(id);
   }

   @Override
   public User findById(Long id) {
      return userDao.findById(id);
   }

   @Override
   public User findByEmail(String email) {
      return userDao.findByEmail(email);
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      User user = userDao.findByEmail(email);
      if (user == null) {
         throw new UsernameNotFoundException("User not found");
      }
      return user;
   }

}
