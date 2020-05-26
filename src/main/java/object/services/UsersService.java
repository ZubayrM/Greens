package object.services;

import lombok.SneakyThrows;
import object.dto.response.errors.ErrorsAuthDto;
import object.dto.response.errors.ErrorsMessageDto;
import object.dto.response.ResultDto;
import object.dto.response.auth.AuthUserResponseDto;
import object.dto.response.auth.UserAuthDto;
import object.dto.response.errors.ErrorsRegisterDto;
import object.model.CaptchaCodes;
import object.model.Users;
import object.repositories.CaptchaCodesRepository;
import object.repositories.PostsRepository;
import object.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Service
public class UsersService {


    private final String PATH_TO_IMAGE = "upload/";

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private CaptchaCodesRepository captchaCodesRepository;

    @SneakyThrows
    public String addImage(Image image) {
        String newPath = generatePathImage();
        ImageIO.write((RenderedImage) image, "png", new File(newPath));
        return newPath;
    }



    public ResultDto login(String email, String password) {

        Optional<Users> user = usersRepository.findByEmail(email);

        if (user.isPresent()){
            if (user.get().getPassword().equals(password)) {
                return new AuthUserResponseDto(generatedUserAuth(user.get()));
            } else
                return new ResultDto(false);
        } else
            return new ResultDto(false);
    }



    public ResultDto check(HttpServletRequest request) {
        String s = request.getHeader("Cookie");
        Optional<Users> user = usersRepository.findById(1);
        if (user.isPresent()) {
            return new AuthUserResponseDto(generatedUserAuth(user.get())); // пока так
        } else
            return new ResultDto(false);
    }

    public ResultDto restore(String email) {
        Optional<Users> user = usersRepository.findByEmail(email);
        if (user.isPresent()){
            String token = UUID.randomUUID().toString();
            user.get().setCode(token);
            String url = "http://localhost:8080/login/change-password/" + user.get().getCode();
            String message = String.format("Для восстановления пароля перейдите по ссылке %s", url );
            mailSenderService.send(user.get().getEmail(), "Password recovery", message);
            return new ResultDto(true);
        } else
            return new ResultDto(false);
    }

    private UserAuthDto generatedUserAuth(Users user) {
        return UserAuthDto.builder()
                .id(user.getId())
                .name(user.getName())
                .photo(user.getPhoto())
                .email(user.getEmail())
                .moderation(user.getIsModerator() > 0)
                .moderationCount(5)///////////////////////////
                .settings(user.getIsModerator() > 0)
                .build();
    }

    private String generatePathImage() {
        Random r = new Random();
        char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        int length = c.length;
        String result = PATH_TO_IMAGE;
        for (int i = 0; i < 4; i++) {
            if (i == 3)
                result += "/";
            else
                result += c[r.nextInt(length)];
        }
        return result + "/img.png";
    }

    public ResultDto password(String code, String password, String captcha, String captchaSecret) {
        Optional<Users> user = usersRepository.findByCode(code);
        if (user.isPresent()){
            CaptchaCodes captchaCodes = captchaCodesRepository.findByCode(captcha);
            if (captchaCodes.getSecretCode().equals(captchaSecret)){
                if (password.length() > 6 ) {
                    user.get().setPassword(password);
                    return new ResultDto(true);
                } else return new ErrorsMessageDto<>(new ErrorsAuthDto(null, "Пароль короче 6-ти символов", null), false);
            } else
                return new ErrorsMessageDto<>(new ErrorsAuthDto(null, null,"Код с картинки введён неверно"), false);
        } else
            return new ErrorsMessageDto<>(
                new ErrorsAuthDto("Ссылка для восстановления пароля устарела.\n" +
                "<a href=http://localhost:8080/auth/restore>Запросить ссылку снова</a>", null, null),false);

    }

    public ResultDto register(String email, String name, String password, String captcha, String captchaSecret) {
        Optional<Users> user = usersRepository.findByEmail(email);

        if (!user.isPresent()){
            if (name.split(" ").length == 2){
                if (password.length() > 6){
                    CaptchaCodes byCode = captchaCodesRepository.findByCode(captcha);
                    if (byCode.getSecretCode().equals(captchaSecret)){
                        return generatedNewUser(name, email, password);
                    } else
                        return new ErrorsMessageDto<>(new ErrorsRegisterDto(null, null, null, "Код с картинки введён неверно"));
                } else
                    return new ErrorsMessageDto<>(new ErrorsRegisterDto(null, null,  "Пароль короче 6-ти символов", null));
            } else
                return new ErrorsMessageDto<>(new ErrorsRegisterDto(null, "Имя указано неверно", null, null));
        } else
            return new ErrorsMessageDto<>(new ErrorsRegisterDto( "Этот e-mail уже зарегистрирован", null, null, null));
    }

    private ResultDto generatedNewUser(String name, String email, String password) {
        Users user = new Users();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRegTime(new Date());
        usersRepository.save(user);
        return new ResultDto(true);
    }

    public ResultDto profileMy(String photo, Integer removePhoto, String name, String email, String password, HttpServletRequest request) {
        String userByEmail = request.getHeader("email");///

        Optional<Users> user = usersRepository.findByEmail(userByEmail);
        if ()
        if (removePhoto > 0){
           // user.get()
        }

        return  new ResultDto(true);
    }


//    private String generateRandomCode(){
//        Random r = new Random();
//        char[] c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
//        int length = c.length;
//        String result = "";
//        for (int i = 0; i < 40; i++) {
//            result += c[r.nextInt(length)];
//        }
//        return result;
//    }
}
