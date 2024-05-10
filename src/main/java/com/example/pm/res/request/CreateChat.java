package com.example.pm.res.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChat {

	private Long projectId;
	
	private List<Long> userIds;
}
