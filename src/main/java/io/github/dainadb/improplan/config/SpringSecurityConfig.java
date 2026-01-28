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
                    .requestMatchers(HttpMethod.GET, "/auth/get").permitAll()
                    .anyRequest().permitAll() //Provisional
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
