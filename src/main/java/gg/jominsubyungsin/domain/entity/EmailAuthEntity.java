package gg.jominsubyungsin.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "email_access")
public class EmailAuthEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private Date expireAt;

	@Column(nullable = true)
	private String code;

	@Column(nullable = false)
	private Boolean auth = false;
}
