package com.al.agency.controllers;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.Action;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.dto.kafka.ObjectAction;
import com.al.agency.entities.Buyer;
import com.al.agency.entities.Seller;
import com.al.agency.repositories.specification.BuyerSpecification;
import com.al.agency.repositories.specification.SellerSpecification;
import com.al.agency.services.AWSS3ServiceImp;
import com.al.agency.services.BuyerService;
import com.al.agency.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/managers/clients")
public class ClientController {
    private SellerService sellerService;
    private BuyerService buyerService;
    private AWSS3ServiceImp awss3Service;
    Transport transportSend;

    @Value("${app.transport}")
    private String nameBean;


    @GetMapping("/sellers")
    public String getAllSellers(Model model,
                                @RequestParam(value = "search",required = false) String search){

        Specification<Seller> filter = Specification.where(null);
        if (search !=null)
            filter = filter.and(SellerSpecification.fioContains(search));

        List<Seller> sellers=sellerService.getSellerWithPaginationAndFilter(filter);
        model.addAttribute("sellers",sellers);
        return "all-seller";
    }

    @GetMapping("/buyers")
    public String getAllBuyers(Model model,
                               @RequestParam(value = "search",required = false) String search){
        Specification<Buyer> filter = Specification.where(null);
        if (search != null)
            filter = filter.and(BuyerSpecification.fioContains(search));

        List<Buyer> buyers = buyerService.getBuyersWithFilter(filter);
        model.addAttribute("buyers", buyers);
        return "all-buyer";
    }

    @GetMapping("/sellers/add")
    public String addFormSeller(Model model){
        model.addAttribute("seller",new Seller());
        model.addAttribute("type", "Добавление");
        return "add-seller";
    }

    @GetMapping("/buyers/add")
    public String addFormBayer(Model model){
        model.addAttribute("buyer",new Buyer());
        model.addAttribute("type", "Добавление");
        return "add-buyer";
    }
    @PostMapping("/sellers/add")
    public String saveSeller(@ModelAttribute(value = "seller") @Valid Seller seller,
                             BindingResult bindingResult,
                             @RequestPart(value= "fileName") final MultipartFile multipartFile,
                             Principal principal){
        if(bindingResult.hasErrors()){
            return "add-seller";
        }
        String newName;
        if (!multipartFile.getOriginalFilename().equals("")) {
            newName = awss3Service.uploadFile(new MultipartFile[]{ multipartFile }).get(0);
        }else {
            newName = "2021-07-16T17:32:57.892523300_test.docx";
        }
        Action action = seller.getId_seller() == null ? Action.ADD : Action.UPDATE;
        seller = sellerService.saveContractAndSeller(seller,newName);
        transportSend.send(new TransportMessage(principal.getName(), action, ObjectAction.SELLER, seller.getId_seller()));
        return "redirect:/managers/clients/sellers";
    }

    @PostMapping("/buyers/add")
    @Transactional
    public String saveBuyer(@ModelAttribute(value = "buyer") @Valid Buyer buyer,
                            BindingResult bindingResult,
                            @RequestPart(value = "fileName") MultipartFile multipartFile,
                            Principal principal){
        if(bindingResult.hasErrors()){
            return "add-buyer";
        }
        String newName;
        if (!multipartFile.getOriginalFilename().equals("")) {
            newName = awss3Service.uploadFile(new MultipartFile[]{ multipartFile }).get(0);
        }else {
            newName = "2021-07-16T17:32:57.892523300_test.docx";
        }
        Action action = buyer.getId_buyer() == null ? Action.ADD : Action.UPDATE;
        buyer = buyerService.saveContractAndBuyer(buyer,newName);
        transportSend.send(new TransportMessage(principal.getName(), action, ObjectAction.BUYER, buyer.getId_buyer()));
        return "redirect:/managers/clients/buyers";
    }

    @GetMapping("/sellers/{id_seller}/add")
    public String addContractSeveralSeller(@PathVariable Long id_seller,
                                           @RequestParam(value = "id_file") Long id_file,
                                           Model model){
        //валидация!!!
        sellerService.addContractForSellerById(id_file, id_seller);
        return "redirect:/managers/clients/sellers";
    }

    @GetMapping("/buyer/update/{id}")
    public String updateBuyer(@PathVariable Long id,
                               Model model){
        Buyer buyer = buyerService.getBuyerById(id);

        model.addAttribute("buyer", buyer);
        model.addAttribute("type", "Обновление");
        return "add-buyer";
    }

    @GetMapping("/seller/update/{id}")
    public String updateSeller(@PathVariable Long id,
                              Model model){
        Seller seller = sellerService.getSellerById(id);

        model.addAttribute("seller", seller);
        model.addAttribute("type", "Обновление");
        return "add-seller";
    }

    @GetMapping("/seller/delete/{id}")
    public String deleteSeller(@PathVariable Long id,
                               Principal principal){
        sellerService.deleteSellerById(id);
        transportSend.send(new TransportMessage(principal.getName(), Action.DELETE, ObjectAction.SELLER, id));
        return "redirect:/managers/clients/sellers";
    }

    @GetMapping("/buyer/delete/{id}")
    public String deleteBuyer(@PathVariable Long id,
                               Principal principal){
        buyerService.deleteBuyerById(id);
        transportSend.send(new TransportMessage(principal.getName(), Action.DELETE, ObjectAction.BUYER, id));
        return "redirect:/managers/clients/buyers";
    }

    @Autowired
    public ClientController(SellerService sellerService,
                            BuyerService buyerService,
                            AWSS3ServiceImp awss3Service,
                            Transport transportSend) {
        this.sellerService = sellerService;
        this.buyerService = buyerService;
        this.awss3Service = awss3Service;
        this.transportSend = transportSend;
    }
}
