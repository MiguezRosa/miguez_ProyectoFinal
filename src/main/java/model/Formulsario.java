package model;

public class Formulsario {
	int id;
	String	usuario;
	String	email;
	String	password;
	
	
	public Formulsario( int id , String u , String e , String p ) {
		this.id=id; 
		this.usuario = u;
		this.email = e;
		this.password = p;
		
		
	}
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
	return this.usuario;	
	}
	
	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setNombre(String usuarioNombre) {
		this.usuario = usuarioNombre;
	}


	public String getemail() {
		return email;
	}


	public void setemail(String email) {
		this.email = email;
	}


	public String getpasspord() {
		return password;
	}


	public void setpassword(String password) {
		this.password = password;
	}


}
