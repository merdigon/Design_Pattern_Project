//package com.configuration;
//
///**
// * Created by pietrek on 10.11.15.
// */
//import java.util.ArrayList;
//import java.util.List;
//
//import com.dao.UserDAO;
//import com.models.UserModel;
//import com.models.UserRole;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//
//@Service("customUserDetailsService")
//public class CustomUserDetailsService implements UserDetailsService{
//
//    @Autowired
//    private UserDAO userDAO;
//
//    @Transactional(readOnly=true)
//    public UserDetails loadUserByUsername(String login)
//            throws UsernameNotFoundException {
//        List<UserModel> userModelList = userDAO.findByLogin(login);
//        System.out.println("UserModel : "+ userModelList);
//        if(userModelList.size()==0){
//            System.out.println("UserModel not found");
//            throw new UsernameNotFoundException("Username not found");
//        }
//        UserModel userModel = userModelList.get(0);
//
//        return new org.springframework.security.core.userdetails.User(userModel.getLogin(), userModel.getPassword(),
//                true, true, true, true, getGrantedAuthorities(userModel));
//    }
//
//
//    private List<GrantedAuthority> getGrantedAuthorities(UserModel userModel){
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//
//        for(UserRole userRole : userModel.getUserRole()){
//            System.out.println("UserModel Role : "+userRole);
//            authorities.add(new SimpleGrantedAuthority("ROLE_"+userRole.getType()));
//        }
//        System.out.print("authorities :"+authorities);
//        return authorities;
//    }
//
//}