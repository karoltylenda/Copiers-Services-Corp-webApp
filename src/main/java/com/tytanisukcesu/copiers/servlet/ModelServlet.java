package com.tytanisukcesu.copiers.servlet;


import com.tytanisukcesu.copiers.dto.ModelDto;
import com.tytanisukcesu.copiers.service.ManufacturerService;
import com.tytanisukcesu.copiers.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/models")
public class ModelServlet {

    private final ModelService modelService;
    private final ModelMapper modelMapper;
    private final ManufacturerService manufacturerService;


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("models", modelService.findAll());
        model.addAttribute("manufacturers",manufacturerService.findAll());
        return "pages/models";
    }

    @PostMapping
    public RedirectView save(ModelDto modelDto){
        modelService.save(convertToEntity(modelDto));
        return new RedirectView("/models");
    }

    @PostMapping(value = "/update")
    public RedirectView update(Long id,ModelDto modelDto){
        modelService.update(id,convertToEntity(modelDto));
        return new RedirectView("/models");
    }

    @PostMapping(value = "/delete")
    public RedirectView delete (ModelDto modelDto){
        com.tytanisukcesu.copiers.entity.Model model = convertToEntity(modelDto);
        modelService.delete(model.getId());
        return new RedirectView("/models");
    }


    private ModelDto convertToDto(com.tytanisukcesu.copiers.entity.Model model) {
        ModelDto modelDto = modelMapper.map(model, ModelDto.class);
        return modelDto;
    }

    private com.tytanisukcesu.copiers.entity.Model convertToEntity(ModelDto modelDto) {
        com.tytanisukcesu.copiers.entity.Model model = modelMapper.map(modelDto, com.tytanisukcesu.copiers.entity.Model.class);
        return model;
    }


}
