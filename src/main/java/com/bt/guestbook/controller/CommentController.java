package com.bt.guestbook.controller;

import com.bt.guestbook.model.Comment;
import com.bt.guestbook.service.CommentService;
import com.bt.guestbook.utils.Approval;
import com.bt.guestbook.utils.FileUploadUtil;
import com.bt.guestbook.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
@ComponentScan
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;


    @Autowired
    private GlobalController globalController;

    @RequestMapping(value = {"/comment/saveComment"}, method = RequestMethod.POST)
    public String saveComment(@ModelAttribute("reqComment") Comment comment,  @RequestParam("image") MultipartFile multipartFile,
                           final RedirectAttributes redirectAttributes) {
        logger.info("/comment/save");
        try {
            comment.setCreateDate(LocalDateTime.now());
            comment.setStatus(Status.ACTIVE.getValue());
            comment.setApproval(Approval.UNAPPROVED.getValue());
            comment.setUserId(globalController.getLoginUser().getId());
            commentService.save(comment);
            redirectAttributes.addFlashAttribute("msg", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "fail");
            logger.error("save: " + e.getMessage());
        }

        return "redirect:/home";
    }



    @RequestMapping(value = {"/comment/saveImage"}, method = RequestMethod.POST)
    public String saveImage(@ModelAttribute("reqComment") Comment comment,  @RequestParam("image") MultipartFile multipartFile,
                              final RedirectAttributes redirectAttributes) throws IOException {
        logger.info("/comment/saveImage");
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            comment.setPhotos(fileName);
            comment.setCreateDate(LocalDateTime.now());
            comment.setStatus(Status.ACTIVE.getValue());
            comment.setApproval(Approval.UNAPPROVED.getValue());
            comment.setUserId(globalController.getLoginUser().getId());
            commentService.save(comment);

            // save the image to system
            //String uploadDir = "user-photos/" + globalController.getLoginUser().getId();
            String uploadDir = "user-photos/";

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);


            redirectAttributes.addFlashAttribute("msg", "success");
            System.out.println(fileName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msg", "fail");
            logger.error("save: " + e.getMessage());
        }

        return "redirect:/home";
    }


    @RequestMapping(value = {"/comment/editComment"}, method = RequestMethod.POST)
    public String editComment(@ModelAttribute("editComment") Comment editComment, Model model) {
        logger.info("/comment/editComment");
        try {
            Comment comment = commentService.findById(editComment.getId());
            if (!comment.equals(editComment)) {
                commentService.update(editComment);
                model.addAttribute("msg", "success");
            } else {
                model.addAttribute("msg", "same");
            }
        } catch (Exception e) {
            model.addAttribute("msg", "fail");
            logger.error("editTask: " + e.getMessage());
        }
        model.addAttribute("editComment", editComment);
        return "edit";
    }


    @RequestMapping(value = "/comment/{operation}/{id}", method = RequestMethod.GET)
    public String todoOperation(@PathVariable("operation") String operation,
                                @PathVariable("id") int id, final RedirectAttributes redirectAttributes,
                                Model model) {

        logger.info("/comment/operation: {} ", operation);
        if (operation.equals("trash")) {
            Comment comment = commentService.findById(id);
            if (comment != null) {
                comment.setStatus(Status.PASSIVE.getValue());
                comment.setApproval(Approval.UNAPPROVED.getValue());
                commentService.update(comment);
                redirectAttributes.addFlashAttribute("msg", "trash");
            } else {
                redirectAttributes.addFlashAttribute("msg", "notfound");
            }
        }
        if (operation.equals("restore")) {
            Comment comment = commentService.findById(id);
            if (comment != null) {
                comment.setStatus(Status.ACTIVE.getValue());
                comment.setApproval(Approval.UNAPPROVED.getValue());
                commentService.update(comment);
                redirectAttributes.addFlashAttribute("msg", "active");
                redirectAttributes.addFlashAttribute("msgText", "Comment " + comment.getDescription() + " Restored Successfully.");
            } else {
                redirectAttributes.addFlashAttribute("msg", "active_fail");
                redirectAttributes.addFlashAttribute("msgText", "Comment Activation failed !!! Task:" + comment.getDescription());

            }
        } else if (operation.equals("delete")) {
            if (commentService.delete(id)) {
                redirectAttributes.addFlashAttribute("msg", "del");
                redirectAttributes.addFlashAttribute("msgText", " Comment deleted permanently");
            } else {
                redirectAttributes.addFlashAttribute("msg", "del_fail");
                redirectAttributes.addFlashAttribute("msgText", " Comment could not deleted. Please try later");
            }
        } else if (operation.equals("edit")) {
            Comment comment = commentService.findById(id);
            if (comment != null) {
                model.addAttribute("editComment", comment);
                return "edit";
            } else {
                redirectAttributes.addFlashAttribute("msg", "notfound");
            }
        } else if (operation.equals("approve")){
            Comment comment = commentService.findById(id);
            if (comment != null) {
                comment.setApproval(Approval.APPROVE.getValue());
                commentService.update(comment);
                redirectAttributes.addFlashAttribute("msg", "approved");
                redirectAttributes.addFlashAttribute("msgText", "Comment " + comment.getDescription() + " Approved Successfully.");
            } else {
                redirectAttributes.addFlashAttribute("msg", "active_fail");
                redirectAttributes.addFlashAttribute("msgText", "Comment Approval failed !!! Task:" + comment.getDescription());

            }
        }
        return "redirect:/admin";
    }

}
