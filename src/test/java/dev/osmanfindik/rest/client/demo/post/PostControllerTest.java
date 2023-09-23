package dev.osmanfindik.rest.client.demo.post;

import dev.osmanfindik.rest.client.demo.client.JsonPlaceholderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JsonPlaceholderService postService;

	@Test
	void findAll () throws Exception {
		Post testPost1 = new Post (1, 1, "testTitle1", "testBody1");
		Post testPost2 = new Post (2, 1, "testTitle2", "testBody2");
		when (postService.findAll ()).thenReturn (List.of (testPost1, testPost2));

		ResultActions result = this.mockMvc
				.perform (get ("/api/posts"))
				.andDo (print ());

		result.andExpect (status ().isOk ())
				.andExpect (jsonPath ("$.[0].id", is (1)))
				.andExpect (jsonPath ("$.[0].userId", is (1)))
				.andExpect (jsonPath ("$.[0].title", is ("testTitle1")))
				.andExpect (jsonPath ("$.[0].body", is ("testBody1")))
				.andExpect (jsonPath ("$.[1].id", is (2)))
				.andExpect (jsonPath ("$.[1].userId", is (1)))
				.andExpect (jsonPath ("$.[1].title", is ("testTitle2")))
				.andExpect (jsonPath ("$.[1].body", is ("testBody2")));
	}

	@Test
	void findById () throws Exception {
		Post testPost = new Post (1, 1, "testTitle", "testBody");
		when (postService.findById (1)).thenReturn (testPost);

		ResultActions result = this.mockMvc
				.perform (get ("/api/posts/1"))
				.andDo (print ());

		result.andExpect (status ().isOk ())
				.andExpect (jsonPath ("$.id", is (1)))
				.andExpect (jsonPath ("$.userId", is (1)))
				.andExpect (jsonPath ("$.title", is ("testTitle")))
				.andExpect (jsonPath ("$.body", is ("testBody")));
	}

	@Test
	void create () {
	}

	@Test
	void update () {
	}

	@Test
	void delete () {
	}
}