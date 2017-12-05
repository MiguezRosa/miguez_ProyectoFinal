package com.example.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.Setting;

import model.Formulsario;

@Controller
public class revistaControler {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping ("/")
	public static String layout() {
		return "index"; 
	}
	
	@GetMapping ("/login")
	public static String loginadmin() {
		return "login"; 
	}
	
	@GetMapping ("/contacto")
	public static String contacto() {
		return "contacto"; 
	}
	
	
	@GetMapping ("/logout")
	public static String logoutAdmin(HttpServletRequest request) {
	
		HttpSession session = request.getSession();
		session.setAttribute("session", null);
		
		return "redirect:/"; 
	}
	
	
	@GetMapping ("/crearNoticia")
	public static String crearNoticia() {
		return "crearNoticia"; 
	}
	
	
		
	@GetMapping ("/noticias")
	public static String casa() {
		return "noticias"; 
	}
	
	@PostMapping ("/noticas/espectaculo")
	public static String noticiaEspectaculo(Model template) {
		return "NoticiasEspectaculo"; 
	}
	
	@PostMapping ("/noticas/deporte")
	public static String noticiaDeporte(Model template) {
		return "noticiasDoporte"; 
	}
	

	
	@PostMapping ("/noticas/politica")
	public static String noticiaPolitica(Model template) {
		return "noticiasPolitica"; 
	}

	@PostMapping ("/noticas/turismo")
	public static String noticiaTurismo(Model template) {
		return "noticiasTurismo"; 
	}

	@PostMapping ("/recibirComentario")
	public static String procesarInfoContacto(	@RequestParam String  nombre ,
												@RequestParam String comentario ,
												@RequestParam String email ,
												Model template)
	{
		
		System.out.println(nombre);
		System.out.println(comentario);
		
		if (nombre.equals("")|| comentario.equals("") || email.equals("") ) {
			
			template.addAttribute("mensajeError", "No puede haber campos vacios");
			template.addAttribute("nombreAnterior" , nombre);
			template.addAttribute("comentarioAnterior" , comentario);
			template.addAttribute("mailAnterior" , email);
			return "contacto";
			
		} 
		else {
			enviarCorreo("no-responder@pepito.com" ,
					email ,
					"Gracias!" ,
					"recibimos tu consulta" );
			enviarCorreo("no-responder@pepito.com" , "miguezrosa3@gmail.com" ,
					"mensaje de contacto de " + nombre ,
					"nombre" + nombre +	"comentario" + comentario + "email" + email);
			return "graciasContacto"; 
		}
	}	
	
	
	
	  public static void enviarCorreo(String de , String para , String asunto , String  contenido){
	        Email from = new Email(de);
	        String subject = asunto;
	        Email to = new Email(para);
	        Content content = new Content("tex/plain" , contenido);
	        Mail mail = new Mail(from, subject, to, content);

	        SendGrid sg = new SendGrid("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");
	        Request request = new Request();
	        try {
	          request.method = Method.POST;
	          request.endpoint = "mail/send";
	          request.body = mail.build();
	          Response response = sg.api(request);
	          System.out.println(response.statusCode);
	          System.out.println(response.body);
	          System.out.println(response.headers);
	        } catch (IOException ex) {
	          System.out.println(ex.getMessage()); ;
	        }
	    }	
	  
	  
	  
	  
	  
	  
	  
	  
	  @GetMapping("/lista-usuarios")
		public static String listaUsuario(Model template, String usuario, String email, String password)
				throws SQLException {
			Connection connection;
			connection = DriverManager.getConnection(Settings.db_url, Settings.db_user, Settings.db_password);

			PreparedStatement ps = connection.prepareStatement("SELECT * FROM listaUsuario;");
			ps.setString(1, usuario);
			ps.setString(2, password);
			ps.setString(3, email);

			ResultSet resultado = ps.executeQuery();

			ArrayList<Formulsario> listausuario;
			listausuario = new ArrayList<Formulsario>();

			// template.addAttribute("listaUsuario", resultado.getString("Usuario"));
			while (resultado.next()) {
				Formulsario miUsuario = new Formulsario(resultado.getInt("id"), resultado.getString("usuario"),
						resultado.getString("email"), resultado.getString("password"));

				listausuario.add(miUsuario);
			}

			template.addAttribute("listaUsuario", listausuario);
			return "listaUsuario";
		}

		@PostMapping("/recibirusuario")
		public static String procesarInfoUsuario(@RequestParam String usuario, @RequestParam String password,
				@RequestParam String email, Model template) throws SQLException {
			if (usuario.equals("") || password.equals("") || email.equals("")) {// si hubo algun error
				// cargar formulario de vuelta
				template.addAttribute("mensajeError", "No puede haber campos vacios");
				template.addAttribute("usuarioAnterior", usuario);
				template.addAttribute("passwordAnterior", password);
				template.addAttribute("emailAnterior", email);

				return "home";
			} else {

				enviarCorreo(email, "revistaWeb@hotmail.com.ar", "Mensaje de contacto de:" + usuario,
						"usuario:" + usuario + "  email:" + email);
				enviarCorreo("revistaWe@hotmail.com.ar", email, " Gracias por contactarte!", " GRACIAS POR REGISTRARTE");

				Connection connection;
				connection = DriverManager.getConnection(Settings.db_url, Settings.db_user, Settings.db_password);
				PreparedStatement qs = connection
						.prepareStatement("INSERT INTO usuarios(usuario,email,password)  VALUES(?,?,?);");
				qs.setString(1, usuario);
				qs.setString(2, email);
				qs.setString(3, password);

				qs.executeUpdate();

				return "graciasPorElRegistro";
			}
		}
	  
	  
	  
	

}