package io.github.dainadb.improplan.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  /**
     * Establece las reglas de protección para las rutas de la API.
     * Determina qué endpoints son de acceso libre y cuáles requieren autorización.
     *
     * @param httpSecurity Constructor para las reglas de seguridad web.
     * @return La cadena de filtros de seguridad ya construida.
     * @throws Exception Si ocurre un fallo durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                
                .authorizeHttpRequests(authorize -> authorize
                
                // Endpoints Públicos (permitAll) 

                // Autenticacióny registro
                .requestMatchers(HttpMethod.POST, "/api/auth/login", 
                                                  "/api/auth/register"
                                                  ).permitAll()
                // Consultas de comunidades, provincias, municipios, temas y roles.
                .requestMatchers(HttpMethod.GET, 
                    "/api/communities/**", 
                    "/api/provinces/**", 
                    "/api/municipalities/**", 
                    "/api/themes/**", 
                    "/api/roles/**"
                ).permitAll()
                // Búsqueda de eventos y detalles.
                .requestMatchers(HttpMethod.GET, 
                    "/api/events/filters", 
                    "/api/events/{id}", 
                    "/api/events/{eventId}/dates/**",
                    "/api/favorites/count/{eventId}"
                ).permitAll()


                   //  Endpoints para Usuarios Autenticados (authenticated) 

                // Logout y obtener/modificar datos del propio usuario.
                .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/users/{id}/profile",
                                                 "/api/users/{id}/change-password"
                                                 ).authenticated()
                // Creación de eventos por parte del usuario.
                .requestMatchers(HttpMethod.POST, "/api/events/create").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/events/user/{email}").authenticated()
                // Gestión de la lista de favoritos.
                .requestMatchers("/api/favorites/**").authenticated()

                
                //  Endpoints para Administradores (hasRole("ROLE_ADMIN")) 

                // Gestión de eventos
                .requestMatchers(HttpMethod.PUT, "/api/events/update/{id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/events/harddelete/{id}",
                                                    "/api/events/softdelete/{id}"
                                                    ).hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/events/publish/{id}").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, 
                    "/api/events/intime/status",
                    "/api/events/discarded", 
                    "/api/events/outtime", 
                    "/api/events/count/**",
                    "/api/events/user/{email}"

                ).hasAuthority("ROLE_ADMIN")
                // Gestión de usuarios.
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasAuthority("ROLE_ADMIN")


                // Cualquier otra petición no definida explícitamente requerirá autenticación.
                .anyRequest().authenticated() 
            )


                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults()

            );
            return httpSecurity.build();
    }


   /**
     * Expone el gestor de autenticación principal de Spring.
     * Esencial para procesar las credenciales de los usuarios.
     *
     * @param authenticationConfiguration La configuración de autenticación de Spring.
     * @return El AuthenticationManager listo para ser inyectado.
     * @throws Exception Si la obtención del gestor falla.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                    throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
    }


   /**
     * Provee el codificador de contraseñas para la aplicación.
     * Utiliza el algoritmo BCrypt para un hashing seguro.
     *
     * @return Una instancia del codificador de contraseñas.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
    }


    /**
     * Configuración global de CORS para permitir peticiones desde el frontend.
     *
     * @return fuente de configuración CORS.
     */

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
