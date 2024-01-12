 package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Song;
import com.example.demo.services.SongService;

@Controller
public class SongController {
	
	@Autowired
	SongService service;

	@PostMapping("/addSong")
	public String addSong(@ModelAttribute Song song) {
		boolean songExists = service.songExists(song.getName());
		if (!songExists) {
			service.addSong(song);
		} else {
			System.out.println("Song already exists");
			// You can redirect to an error page or add a flash message to notify the user
			// Example: return "redirect:/addSong?error=songexists";
		}
		return "adminHome"; // Redirect to the admin home page
	}
	  
	@GetMapping("/ViewSong")
	public String viewSongs(Model model) {
		List<Song> songList=service.fetchAllSongs();
		model.addAttribute("songs", songList);
		return "displaySong";
	}
	
	@GetMapping("/playSong")
	public String playSongs(Model model) {
		boolean premiumUser=false;
		if(premiumUser==true) {
			List<Song> songList=service.fetchAllSongs();
			model.addAttribute("songs", songList);
			return "displaySong";
			}else {
		return "makePayment";
			}
		
	}
}

