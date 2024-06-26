package com.example.authservice.Service;

import com.example.authservice.Exception.ExpiredTokenException;
import com.example.authservice.Exception.RepeatedUsername;
import com.example.authservice.JWTUtils.TokenManger;
import com.example.authservice.Model.EndUser;
import com.example.authservice.Model.EndUserDto;
import com.example.authservice.Model.TokenDto;
import com.example.authservice.Model.TokenPack;
import com.example.authservice.Repository.EndUserRepository;
import com.example.authservice.Repository.TokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EndUserDetailsService {

    private final EndUserRepository endUserRepository;
    private final TokenRepository tokenRepository;
    private final TokenManger tokenManger;

    public EndUserDetailsService(EndUserRepository endUserRepository,TokenRepository tokenRepository,TokenManger tokenManger) {
        this.endUserRepository = endUserRepository;
        this.tokenRepository = tokenRepository;
        this.tokenManger = tokenManger;
    }

    public EndUserRepository getEndUserRepository() {
        return endUserRepository;
    }

    public TokenRepository getTokenRepository() {
        return tokenRepository;
    }

    public String createUser(EndUserDto endUserDto) throws Exception {
        if(endUserRepository.getEndUserByUsername(endUserDto.getUsername()).isPresent()){
            throw new RepeatedUsername("username already taken!");
        }
        EndUser endUser = new EndUser();
        endUser.setUsername(endUserDto.getUsername());
        endUser.setPassword(hashString(endUserDto.getPassword()));
        endUser.setAuthorized(false);
        endUser.setRole("USER");
        endUser.setAuthority(new SimpleGrantedAuthority("ROLE_USER"));
        endUser.setRegisterDate(new Date());
        endUserRepository.save(endUser);
        return "new user created!\nplease wait until the admin authenticate your account";

    }
    public TokenDto generateToken(String tokenName , String expireDate , EndUser endUser){
        if(endUser == null){
            throw new ExpiredTokenException("username is null!");
        }
        if(tokenRepository.getTokenPackByName(tokenName) != null){
            throw new ExpiredTokenException("this name for token is already taken!");
        }
        TokenPack tokenPack = new TokenPack();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date expire;
        try {
            expire = simpleDateFormat.parse(expireDate);
        } catch (ParseException e) {
            System.out.println("error in here!!!!");
            throw new ExpiredTokenException("error in time format!");
        }
        tokenPack.setTokenValue(tokenManger.generateToken(endUser.getUsername(), expire ));
        tokenPack.setName(tokenName);
        tokenPack.setExpireDate(expire);
        tokenPack.setOwnerUsername(endUser.getUsername());
        tokenRepository.save(tokenPack);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setTokenValue(tokenPack.getTokenValue());
        tokenDto.setName(tokenName);
        tokenDto.setDate(expireDate);
        return tokenDto;

    }
    public EndUser getUserById(long id){
        Optional<EndUser> user = endUserRepository.getEndUserById(id);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("this user doesn't exist!");
        }else{
            return user.get();
        }
    }
    public EndUser getUserByUsername(String username){
        Optional<EndUser> user = endUserRepository.getEndUserByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("this user doesn't exist!");
        }else{
            return user.get();
        }
    }
    public String validatePassword(String username, String password) throws NoSuchAlgorithmException {
        EndUser endUser = endUserRepository.getEndUserByUsername(username).get();
        if(endUser == null || hashString(password).equals(endUser.getPassword()) ){
            TokenPack initToken = new TokenPack();
            initToken.setOwnerUsername(endUser.getUsername());
            initToken.setName("init_0_"+endUser.getUsername());
            initToken.setExpireDate(new Date(System.currentTimeMillis()+ 1000*60*5));
            initToken.setTokenValue(tokenManger.generateToken(endUser.getUsername() , initToken.getExpireDate()));
            tokenRepository.save(initToken);
            return (
                    "token = "+initToken.getTokenValue());

        }else {
            throw new UsernameNotFoundException("username and password doesn't match!");
        }
    }

    public  String hashString(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : encodedHash) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public void enableUser(EndUser admin,String username, boolean condition) throws AccessDeniedException {
        if(admin.getRole().equals("ADMIN")){
            Optional<EndUser> endUser = endUserRepository.getEndUserByUsername(username);
            if(endUser.isPresent()) {
                endUser.get().setAuthorized(condition);
                endUserRepository.save(endUser.get());
            }else{
            throw new UsernameNotFoundException("this username doesn't exist!");
            }
        }else {
            throw new AccessDeniedException("You don't have access to this page!");
        }
    }


    public String getAllTokens(EndUser endUser){
        Collection<TokenPack> tokens = tokenRepository.findByOwnerUsername(endUser.getUsername());
        if(tokens.isEmpty()){
            return "this user hasn't created any tokens yet!";
        }
        return  "{\ntokens count :" + tokens.size()+"\n"+ tokens.stream().map(tokenPack -> "{\ntoken name : "+ tokenPack.getName()+"\n" +
                "expire date : "+ tokenPack.getExpireDate()+"\n" +
                "token key ; "+ tokenPack.getTokenValue().substring(0,10)+"*****\n}").collect(Collectors.joining("\n"))+"\n}";
    }
    @Transactional
    public String  removeToken(EndUser endUser, String tokenName, String tokenValue) {
        TokenPack tokenPack = tokenRepository.getTokenPackByName(tokenName);
        if (tokenPack == null) {
            throw new ExpiredTokenException("no token exists with this name !");
        }
        if(tokenPack.getExpireDate().before(new Date(System.currentTimeMillis()))){
            throw new ExpiredTokenException("this token has  already been expired!");
        }
        if(!tokenPack.getOwnerUsername().equals(endUser.getUsername())){
            throw new ExpiredTokenException("this token doesn't belong to you!");
        }
        if(!tokenPack.getTokenValue().equals(tokenValue)){
            throw new ExpiredTokenException("mismatch in token value!");
        }
        tokenRepository.deleteByName(tokenName);
        return "token deleted successfully!";
    }
    public String getAllUsers(){
        ArrayList<EndUser> users = endUserRepository.getAllByIdNotNull();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(users);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "An error occurred while converting users to JSON.";
        }
    }


}
