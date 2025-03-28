# Spring Security?
  공식 문서 : https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html

API가 실행될 때 마다 사용자가 인증, 이 인증을 구현해놓은 것이 SpringSecurity
스프링 하위 framework, 누가 인증하는지 어떤것을 할수 있는지를 담당하는 framework
주로 Servlet Filter와 이들로 구성된 FilterChain으로의 구성된 위임 모델을 사용

# 1 basic terminology
  Principal(접근 주체) : 보호된 대상에 접근하는 유저
  Authentication(인증) : 인증은 '증명하다'라는 뜻으로, 유저 아이디와 비밀번호를 이용하여 로그인하는 과정
  Authorization(인가) : '권한부여'나 '허가'라는 뜻으로, 대상이 특정 목적을 실행하도록 acess하는것을 의미
  권한 : 인증된 주체가 애플리케이션의 동작ㅇ르 수행할 수 있도록 허락되었는지 결정할 때 사용

# 2 feature and structure
  보안과 관련하여 체계적으로 많은 옵션을 제공하려 평리하게 사용 가능
  Filter 기반으로 동작하여 MVC와 구분하여 관리 및 동작
  Annotation을 통해 간단하게 설정 가능
  Spring Security는 기본적으로 Session & Cookie 방식으로 인증
  (#) Annotation
    다른 프로그램에게 유용한 정보를 제공하기 위해 사용되는 것으로 주석과 같은 의미
    컴파일러에게 문법 에러를 체크하도록 정보를 제공
    프로그램을 빌드할 때 코드를 자동으로 생성할 수 있도록 정보를 제공
    런타임에 특정 기능을 실행하도록 정보를 제공
    인증 관리자(Authentication Manager)와 접근 결정 관리자(Acess Decision Manger)를 통해,
    사용자의 리소스 접근을 관리
    인증관리자는 UsernamePasswordAuthenticationFilter
    접근 결정 관리자는 FilterSecurityInterceptor가 수행

# 3 Spring Security Filter
  SecurityContextPersistenceFilter : SecurityContextRepository에서 SecurityContext를 가져오거나 저장하는 역할
  LogoutFilter : 설정된 로그아웃 URL로 오는 요청을 감시하며, 해당 유저를 로그아웃 처리
  (UsernamePassword)AuthenticationFilter : (아이디어와 비밀번호를 사용하는 form 기반 인증) 설정된 로그인 URL로 오는 요청을 감시하며, 유저 인증 처리
    o) 인증 성공 시, 얻은 Authentication 객체를 SecurityContext에 저장 후 AuthenticationSucessHandler 실행
    o) 인증 실패 시, AuthenticationFailureHandler 실행
  DefaultLoginpageGeneratingFilter : form기반 또는 OpenID기반 인증에 사용하는 가상 URL에 대한 요청을 감시하고 로그인 폼 기능을 수행하는데 필요한 HTML을 생성
  BasicAuthenticationFilter : HTTP 기본 인증 헤더를 감시하여 처리
  RequestCacheAwareFilter : 로그인 성공 이후, 원래 인증 요청에 의해 가로채어진 사용자의 원래 요청을 재구성하는데 사용
  SecurityContextHolderAwareRequestFilter : HttpServletRequestWrapper를 상속한 SecurityContextHolderAwareRequestWapper클래스로 HttpServletRequest 정보를 감쌈, 
  SecurityContextHolderAwareRequestWrapper클래스는 필터 체인상의 다음 필터들에게 부가정보를 제공
  AnonymouseAuthenticationFilter : 이 필터가 회출되는 시점까지 사용자 정보가 인증되지 않았다면 인증토큰에 사용자가 익명 사용자로 나타남
  SessionManagementFilter : 인증된 주체를 바탕으로 세션 트래킹을 처리해 단일 주체와 관련한 모든 세션들이 트래킹되도록 도움
  ExceptionTranslationFiler : 보호된 요청을 처리하는 중에 발생할 수 있는 예외의 기본 라우팅과 위임, 전달하는 역할
  FilterSecurityInterceptor : AcessDecisionManager로 권한부여처리를 위임함으로써 접근 제어 결정을 쉽게 해줌

# 4 Spring Security 인증관련 Architecture
  1) client가 로그인을 시도
  2) AuthenticationFilter는 AuthenticationManager, AuthenticationProvider(s), UserDetailsService를 통해 DB에서 사용자 정보를 읽어옴, UserDetailsService가 interface(interface를 구현한 Bean을 생성하면 해당 빈 사용), 어떤 DB로 부터 읽어올지 개발자가 결정 가능
  3) UserDetailsService는 로그인한 ID에 해당하는 정보를 DB에서 읽어들여 UserDeatils 정보를 세션에 저장
  4) Spring Security는 인메모리 세션저장소인 SecurityContextHOlder에 UserDetails정보를 저장
  5) client에게 session ID(Jession ID)와 함께 응답을 함
  6) 이후 요청에서는 요청 cookie에서 Jession ID정보를 통해 이미 로그이니 정보가 저장되어 있는지 확인, 이미 저장되어 있고 유효하면 인증처
    
# 회원 도메인 생성

  #UserDetails 클래스를 상속하는 User 클래스 생성
  @Table(name = "users")
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Getter
  @Entity
  public class User implements UserDetails {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "id", updatable = false)
      private Long id;
  
      @Column(name = "email", nullable = false, unique = true)
      private String email;
  
      @Column(name = "password")
      private String password;
  
      @Builder
      public User(String email, String password, String auth) {
          this.email = email;
          this.password = password;
      }
  
      @Override // 권한 반환
      public Collection<? extends GrantedAuthority> getAuthorities() {
          return List.of(new SimpleGrantedAuthority("user"));
      }
  
      // 사용자의 id 반환(고유한 값)
      @Override
      public String getUsername() {
          return email;
      }
  
      // 사용자의 패스워드 반환
      @Override
      public String getPassword() {
          return password;
      }
  
      // 계정 만료 여부 반환
      @Override
      public boolean isAccountNonExpired() {
          return true; // true : 만료 X
      }
  
      // 계정 잠금 여부 반환
      @Override
      public boolean isAccountNonLocked() {
          return true; // true : 잠금 X
      }
  
      // 패스워드 만료 여부 반환
      @Override
      public boolean isCredentialsNonExpired() {
          return true; // true : 만료 X
      }
  
      // 계정 사용 가능 여부 반환
      @Override
      public boolean isEnabled() {
          return true; // true : 사용 가능
      }
  }
  
  getAuthorities() : 사용자가 가지고 있는 권한 목록 반환
  getUsername() : 사용자를 식별할 수 있는 사용자 이름 반환, 사용되는 값은 반드시 고유해야 함
  getPassword() : 사용자의 비밀번호 반환, 반드시 암호화해서 저장
  isAccountNonExpired() : 계정이 만료되었는지 확인, 만료되지 않았을 시 true 반환
  isAccountNonLocked() : 계정이 잠금되었는지 확인, 잠금되지 않았을 시 true 반환
  isCredentialsNonExpired() : 비밀번호가 만료되었는지 확인, 만료되지 않았을 시 true 반환
  isEnabled() : 계정이 사용 가능한지 확인, 사용 가능할 시 true 반환

  ## Repository 생성
  spring security를 이용해 사용자 정보를 가져오기 위해 spring security가 email을 전달 받아야 함
  
  public interface UserRepository extends JpaRepository<User, Long> {
    Optinial<User> findByEmail(String email); // email로 사용자 정보 가져옴
  }
  
  ## Service 생성
  UserDetailsService interface를 구현하고, loadUserByUsername() method를 overriding해서 사용자 정보를 가져오는 로직
  
  @RequiredArgsConstructor
  @Service
  public class UserDetailService implemnts UserDetailsService {
    private final UserRepository userRepository;
  
    @Override
    public User loadUserByUsername(String email) {
      return userRepository,findByEmail(email).orElseThrow(() -> new IllegalArgumentException(email));
    }
  
  ## WebSecurityConfig
  
  @RequiredArgsConstructor
  @EnableWebSecurity
  @Configuration
  public class WebSecurityConfig {
    private final UserDetailsService userServiice;
  
    //spring security 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
      return (web) -> web.ignoring().requestMachers(toH2Console()).requestMatchers("/static/**");
  
    // 특정 HTTP 요청에 대한 웹 기반 보안 구성 //현재 패스
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
              return http
                  .authorizeHttpRequests()
                  .requestMatchers("/login", "/signup", "/user").permitAll()
                  .anyRequest().authenticated()
                  .and()
                  .formLogin() // 폼 기반 로그인 설정
                  .loginPage("/login")
                  .defaultSuccessUrl("/home")
                  .and()
                  .logout() // 로그아웃 설정
                  .logoutSuccessUrl("/login")
                  .invalidateHttpSession(true)
                  .and()
                  .csrf().disable() // 로컬에서 확인을 위해 csrf 비활성화
                  .build();
    }
  
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception{
      DaoAuthenticationProvide daoAuthenticationProvide = neew DaoAuthenticationProvider();
  
      daoAuthenticationProvider.setUserDetailsService(userService);
      daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
  
      return daoAuthenticationProvider;
    }
  
    // 패스워드 인코더로 사용할 빈 등록
    // password 암호화를 위해 BCryptPasswordEncoder 클래스를 생성하여 빈에 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
      return new BCryptPasswordEncoder();
    }

# 회원 가입 구현
  ## DTO 생성
  
    @Getter
    @Setter
    public class AddUserRequest {
      private String email;
      private String password;
  }

  ## Service 생성
  
  패스워드를 BCryptPasswordEncoder를 사용해서 암호화한 후에 저장
  @RequiredArgsConstructor
  @Service
  public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
      return userRepository.save(User.builder().email(dto.getEmail()).password(bCryptPasswordEncoder.encode(dto.getPassword()).build()).getId();
  }

  ## Controller 생성
  redirect : 접두사를 붙이면 회원 가입 처리가 끝난 후 무조건 /login URL로 이동
  @RequiredArgsConstructor
  @Controller
  public clss UserApiController {
    private final UserService userService;

    #PostMapping("/user")
    public String signup(addUserRequest request( {
      userService.save(request);
      return "redirect:/login";
    }
  }

  ## ViewController 생성
  @Controller
  public class UserViewController {
    @GetMapping("/home")
    public String home() {
      return "home";
    }

    @GetMaaping("/login")
    public String login(){
      return "login";
    }

    @GetMapping("/singup")
    public String singup() {
      return "singup";
    }
  }

# 로그아웃 구현
  #Controller 추가
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpSeveletResponse response) {
      new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
  return "redirect:/login";
  }


  





