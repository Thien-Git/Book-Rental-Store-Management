package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.StaffDTO;
import com.example.brmproject.domain.entities.ERole;
import com.example.brmproject.domain.entities.StaffEntity;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.repositories.StaffEntityRepository;
import com.example.brmproject.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffServiceImp implements StaffService{

    private ModelMapper modelMapper;
    //DI here ...add inteface
    private  StaffEntityRepository staffEntityRepository;
    @Autowired
    public StaffServiceImp(ModelMapper modelMapper, StaffEntityRepository staffEntityRepository) {
        this.modelMapper = modelMapper;
        this.staffEntityRepository = staffEntityRepository;
    }

    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        StaffEntity staff = staffEntityRepository.save(mapToEntity(staffDTO));
        StaffEntity newStaff = staffEntityRepository.findById(staff.getId()).orElseThrow(()->new ResourceNotFoundException("Staff","Id",String.valueOf(staff.getId())));

       return mapToDTO(newStaff);


    }

    @Override
    public List<StaffDTO> findAll() {
        List<StaffEntity> listStaff = staffEntityRepository.findAll();

        List<StaffEntity> staffWithoutAdminRole = listStaff.stream()
                .filter(staff -> staff.getUserByUserId().getRoles().stream()
                        .noneMatch(role -> role.getName() == ERole.ADMIN))
                .collect(Collectors.toList());


        List<StaffDTO>list = staffWithoutAdminRole.stream().map(customers->mapToDTO(customers)).collect(Collectors.toList());

        return list;
    }



    //map to dto
    public StaffDTO mapToDTO(StaffEntity staff) {
        StaffDTO StaffDTO = modelMapper.map(staff, StaffDTO.class);
        return StaffDTO;

    }

    public StaffEntity mapToEntity(StaffDTO staffDTO) {
        StaffEntity staff = modelMapper.map(staffDTO, StaffEntity.class);
        return staff;

    }
    
    
}
