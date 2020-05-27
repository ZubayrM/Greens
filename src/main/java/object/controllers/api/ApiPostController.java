package object.controllers.api;

import object.dto.response.ResultDto;
import object.dto.response.post.ListPostResponseDto;
import object.dto.response.post.PostAllCommentsAndAllTagsDto;
import object.dto.response.tag.TagsDto;
import object.model.enums.Mode;
import object.model.enums.ModerationStatus;
import object.services.PostCommentsService;
import object.services.PostVotesService;
import object.services.PostsService;
import object.services.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private PostsService postsService;
    private PostVotesService postVotesService;
    private PostCommentsService postCommentsService;
    private TagsService tagsService;

    @Autowired
    public ApiPostController(PostsService postsService, PostVotesService postVotesService, PostCommentsService postCommentsService, TagsService tagsService) {
        this.postsService = postsService;
        this.postVotesService = postVotesService;
        this.postCommentsService = postCommentsService;
        this.tagsService = tagsService;
    }

    @GetMapping("/post")
    public ResponseEntity<?> getAllPosts(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam String mode){
        ListPostResponseDto dto = postsService.getListPostResponseDtoByMode(offset, limit, Mode.valueOf(mode.toUpperCase()));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/search")
    public ResponseEntity getPostsBySearch(@RequestParam Integer offset,
                                           @RequestParam Integer limit,
                                           @RequestParam String query){
        ListPostResponseDto dto = postsService.getListPostResponseDtoBySearch(offset, limit, query);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity getPost(@PathVariable Integer id){
        PostAllCommentsAndAllTagsDto dto = postsService.getPostAllCommentsAndAllTagsDto(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/byDate")
    public ResponseEntity getPostsByDate(@RequestParam Integer offset,
                                         @RequestParam Integer limit,
                                         @RequestParam String date){
        ListPostResponseDto dto = postsService.getListPostResponseDtoByDate(offset, limit, date);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/byTag")
    public ResponseEntity getPostsByTag(@RequestParam Integer offset,
                                        @RequestParam Integer limit,
                                        @RequestParam String tag){
        ListPostResponseDto dto = postsService.getListPostResponseDtoByTag(offset, limit, tag);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("post/moderation")
    public ResponseEntity getPostsModeration(@RequestParam Integer offset,
                                             @RequestParam Integer limit,
                                             @RequestParam String status){
        ListPostResponseDto dto = postsService.getPostDtoModeration(offset, limit, ModerationStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/post/my")
    public ResponseEntity getMyPosts(@RequestParam Integer offset,
                                     @RequestParam Integer limit,
                                     @RequestParam String status){
        ListPostResponseDto dto = postsService.getMyListPost(offset, limit, status);
        return ResponseEntity.ok(dto);
    }

    //NO
    @PostMapping("/post")
    public ResponseEntity addPost(@RequestParam Integer active,
                                  @RequestParam Map<Integer, String> tags,
                                  @RequestParam String text,
                                  @RequestParam String time,
                                  @RequestParam String title,
                                  HttpServletRequest request){
        ResultDto dto = postsService.addPost(time, active, title, text, tags);
        return ResponseEntity.ok(dto);
    }


//    @PostMapping("/post")
//    public ResponseEntity addPost(@RequestBody NewPostDto newPostDto){
//        //ResultPostDto dto = postsService.addPost(newPostDto);
//        return ResponseEntity.ok(null);
//    }

    //NO
    @PutMapping("/post/{id}")
    public ResponseEntity update(@RequestParam String time,
                                 @RequestParam Integer active,
                                 @RequestParam String title,
                                 @RequestParam String text,
                                 @RequestParam String tags,
                                 @PathVariable Integer id){
        ResultDto dto = postsService.update(time, active, title, text, tags, id);
        return ResponseEntity.ok(dto);
    }

    //NO
    @PostMapping("/comment")
    public ResponseEntity addComment(@RequestParam("parent_id") Integer parentId,
                                     @RequestParam("post_id") Integer postId,
                                     @RequestParam String text ){
        //ResultPostCommentDto dto = postsService.addComment(parentId, postId, text);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/tag")
    public ResponseEntity getTags(@RequestParam @Nullable String query){
        TagsDto dto = tagsService.getTagByQuery(query);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/post/like")
    public ResponseEntity like(@RequestParam("post_id") Integer postId, HttpServletRequest request){
        ResultDto dto = postVotesService.like(postId, request);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/post/dislike")
    public ResponseEntity dislike(@RequestParam("post_id") Integer postId, HttpServletRequest request){
        ResultDto dto = postVotesService.disLike(postId, request);
        return ResponseEntity.ok(dto);
    }





}
