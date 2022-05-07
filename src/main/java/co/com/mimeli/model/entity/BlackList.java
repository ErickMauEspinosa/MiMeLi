package co.com.mimeli.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "blacklist")
public class BlackList {
	@Id
	private String ip;
}
