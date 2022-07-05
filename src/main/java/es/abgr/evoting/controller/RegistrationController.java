package es.abgr.evoting.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.abgr.evoting.Constants;
import es.abgr.evoting.service.ProcedureService;
import es.abgr.evoting.service.VotacionValidationService;
import es.abgr.evoting.service.VoterService;
import es.abgr.evoting.web.dto.VoterRegistrationDto;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

	private VoterService voterService;
	private ProcedureService procedureService;

	@Autowired
	ResourceBundleMessageSource messageSource;

	public RegistrationController(VoterService voterService, ProcedureService procedureService) {
		super();
		this.voterService = voterService;
		this.procedureService = procedureService;
	}

	@ModelAttribute("voter")
	public VoterRegistrationDto voterRegistrationDto() {
		return new VoterRegistrationDto();
	}

	@GetMapping
	public String showRegistrationForm(@ModelAttribute("voter") VoterRegistrationDto registrationDto) {
		registrationDto.setProcedures(procedureService.findAllActiveWebProcedures());
		return "registration";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("voter") @Valid VoterRegistrationDto registrationDto,
			BindingResult result) {

		if (registrationDto.getProcedure() == null) {
			result.addError(new ObjectError("registroError",
					messageSource.getMessage(Constants.ERROR_PROCESO, null, Locale.getDefault())));
		}
		if (voterService.findByUsername(registrationDto.getUsername()) != null) {
			result.addError(new ObjectError("registroError",
					messageSource.getMessage(Constants.USUARIO_YA_EXISTE, null, Locale.getDefault())));
		}
		if (result.hasErrors()) {
			registrationDto.setProcedures(procedureService.findAllActiveWebProcedures());
			return "registration";
		}
		voterService.save(registrationDto);
		return "redirect:/login";
	}

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//    	Map <String, String> errors = new HashMap<>();
//    	ex.getBindingResult().getAllErrors().forEach((error) -> {
//    		FieldError fError = (FieldError) error;
//    		errors.put(fError.getField(), fError.getDefaultMessage());
//    	});
//    	return errors;
//    }
}
