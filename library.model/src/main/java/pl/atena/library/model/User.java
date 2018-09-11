package pl.atena.library.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, unique = true)
	private Long id;

	@NotNull
	@Size(min = 2, max = 20)
	@Column(length = 20, nullable = false, unique = true)
	private String name;

	@NotNull
	@Size(min = 2, max = 20)
	@Column(length = 20, nullable = false, unique = true)
	private String surname;
	
	@NotNull
	@Size(min = 5)
	@Column(length = 20, unique = true)
	private String email;
}
