package dev.osmanfindik.rest.client.demo.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.osmanfindik.rest.client.demo.client.JsonPlaceholderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JsonPlaceholderService postService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenListOfPosts_whenGetALL_thenReturnAllPosts () throws Exception {
		Post testPost1 = new Post (1, 1, "testTitle1", "testBody1");
		Post testPost2 = new Post (2, 1, "testTitle2", "testBody2");
		when (postService.findAll ()).thenReturn (List.of (testPost1, testPost2));

		ResultActions result = this.mockMvc
				.perform (get ("/api/posts"))
				.andDo (print ());

		result.andExpect (status ().isOk ())
				.andExpect (jsonPath ("$.[0].id", is (testPost1.id ())))
				.andExpect (jsonPath ("$.[0].userId", is (testPost1.userId ())))
				.andExpect (jsonPath ("$.[0].title", is (testPost1.title ())))
				.andExpect (jsonPath ("$.[0].body", is (testPost1.body ())))
				.andExpect (jsonPath ("$.[1].id", is (testPost2.id ())))
				.andExpect (jsonPath ("$.[1].userId", is (testPost2.userId ())))
				.andExpect (jsonPath ("$.[1].title", is (testPost2.title ())))
				.andExpect (jsonPath ("$.[1].body", is (testPost2.body ())));
	}

	@Test
	void givenPost_whenGetPostById_thenReturnPost () throws Exception {
		Post testPost = new Post (1, 1, "testTitle", "testBody");
		when (postService.findById (1)).thenReturn (testPost);

		ResultActions result = this.mockMvc
				.perform (get ("/api/posts/" + testPost.id ()))
				.andDo (print ());

		result.andExpect (status ().isOk ())
				.andExpect (jsonPath ("$.id", is (testPost.id ())))
				.andExpect (jsonPath ("$.userId", is (testPost.userId ())))
				.andExpect (jsonPath ("$.title", is (testPost.title ())))
				.andExpect (jsonPath ("$.body", is (testPost.body ())));
	}

	@Test
	void givenNoPost_whenGetPostById_thenThrowException () {
		int postId = 333;
		when (postService.findById (postId))
				.thenThrow (HttpClientErrorException
						.create (HttpStatus.NOT_FOUND, "not found",
								null, null, null));

		assertThatThrownBy (() -> mockMvc.perform (get ("/api/posts/" + postId)))
				.hasCauseInstanceOf (HttpClientErrorException.class)
				.hasMessageContaining ("not found");
	}

	@Test
	void givenPost_whenCreate_thenReturnNewPost () throws Exception {
		Post testPost = new Post (1, 1, "testTitle", "testBody");
		when (postService.create (any (Post.class))).thenReturn (testPost);

		ResultActions result = this.mockMvc
				.perform (post ("/api/posts")
						.contentType (MediaType.APPLICATION_JSON)
						.content (objectMapper.writeValueAsString (testPost)))
				.andDo (print ());

		result.andExpect (status ().isCreated ())
				.andExpect (jsonPath ("$.id", is (testPost.id ())))
				.andExpect (jsonPath ("$.userId", is (testPost.userId ())))
				.andExpect (jsonPath ("$.title", is (testPost.title ())))
				.andExpect (jsonPath ("$.body", is (testPost.body ())));
	}

	@Test
	void givenPost_whenUpdate_thenReturnUpdatedPost () throws Exception {
		Post testPost = new Post (1, 1, "testTitle", "testBody");
		when (postService.update (eq (testPost.id ()), any (Post.class))).thenReturn (testPost);

		ResultActions result = this.mockMvc
				.perform (put ("/api/posts/" + testPost.id ())
						.contentType (MediaType.APPLICATION_JSON)
						.content (objectMapper.writeValueAsString (testPost)))
				.andDo (print ());

		result.andExpect (status ().isOk ())
				.andExpect (jsonPath ("$.id", is (testPost.id ())))
				.andExpect (jsonPath ("$.userId", is (testPost.userId ())))
				.andExpect (jsonPath ("$.title", is (testPost.title ())))
				.andExpect (jsonPath ("$.body", is (testPost.body ())));
	}

	@Test
	void givenPost_whenDelete_thenReturnNoContent () throws Exception {
		int postId = 1;

		ResultActions result = this.mockMvc
				.perform (delete ("/api/posts/" + postId))
				.andDo (print ());

		result.andExpect (status ().isNoContent ());
	}
}