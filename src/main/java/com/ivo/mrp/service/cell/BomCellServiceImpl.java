package com.ivo.mrp.service.cell;

import com.ivo.mrp.entity.direct.cell.*;
import com.ivo.mrp.repository.cell.BomCellProductRepository;
import com.ivo.mrp.repository.cell.BomCellRepository;
import com.ivo.mrp.repository.cell.CellMpsModeRepository;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class BomCellServiceImpl implements BomCellService {

    private RestService restService;

    private CellMpsModeRepository cellMpsModeRepository;

    private BomCellProductRepository bomCellProductRepository;

    private BomCellRepository bomCellRepository;

    private CellMaterialService cellMaterialService;

    @Autowired
    public BomCellServiceImpl(RestService restService,
                              CellMpsModeRepository cellMpsModeRepository, BomCellProductRepository bomCellProductRepository,
                              BomCellRepository bomCellRepository,
                              CellMaterialService cellMaterialService) {
        this.restService = restService;
        this.cellMpsModeRepository = cellMpsModeRepository;
        this.bomCellProductRepository = bomCellProductRepository;
        this.bomCellRepository = bomCellRepository;
        this.cellMaterialService = cellMaterialService;
    }

    @Override
    public void syncCellMpsMode() {
        log.info("同步CELL的MPS机种命名 >> START");
        List<Map> mapList = restService.getCellMpsMode();
        if(mapList==null || mapList.size()==0) return;
        List<CellMpsMode> cellMpsModeList = new ArrayList<>();
        for(Map map : mapList) {
            CellMpsMode cellMpsMode = new CellMpsMode();
            cellMpsMode.setProduct((String) map.get("product"));
            cellMpsMode.setCellMtrl((String) map.get("Material_FK"));
            cellMpsMode.setId(cellMpsMode.getProduct()+"_"+cellMpsMode.getCellMtrl());
            cellMpsModeList.add(cellMpsMode);
        }
        cellMpsModeRepository.saveAll(cellMpsModeList);
        log.info("同步CELL的MPS机种命名 >> END");
    }

    @Override
    public void syncBomCellProduct() {
        List<String> productList = cellMpsModeRepository.getProduct();
        if(productList == null || productList.size() == 0) return;
        List<BomCellProduct> bomCellProductList = new ArrayList<>();
        for(String product : productList) {
            BomCellProduct bomCellProduct = getBomCellProduct(product);
            if(bomCellProduct == null) {
                bomCellProduct = new BomCellProduct();
                bomCellProduct.setProduct(product);
                bomCellProduct.setVerify(false);
                bomCellProductList.add(bomCellProduct);
            }
        }
        bomCellProductRepository.saveAll(bomCellProductList);
    }

    @Override
    public void syncBomCell() {
        List<BomCellProduct> bomCellProductList = getBomCellProduct();
        for(BomCellProduct bomCellProduct : bomCellProductList) {
            if(bomCellProduct.isVerify()) continue;

            String product = bomCellProduct.getProduct();
            List<String> cellMtrlList = getCellMtrl(product);
            List<CellMaterial> cellMaterialList = cellMaterialService.getCellMaterialMaster(cellMtrlList);

            if(cellMaterialList == null || cellMaterialList.size() ==0) continue;
            List<BomCell> bomCellList = new ArrayList<>();
            for(CellMaterial cellMaterial : cellMaterialList) {
                BomCell bomCell = new BomCell();
                String material = cellMaterial.getMTRL_ID();
                bomCell.setId(product+"_"+material);
                bomCell.setProduct(product);
                bomCell.setMaterial(material);
                bomCell.setUsageQty(cellMaterial.getUSAGEQTY());
                bomCell.setMeasureUnit(cellMaterial.getMEASURE_UNIT());

                bomCell.setMaterialName(cellMaterial.getMaterialName());
                bomCell.setMaterialGroup(cellMaterial.getMaterialGroup());
                bomCell.setMaterialGroupName(cellMaterial.getMaterialGroupName());
                bomCellList.add(bomCell);
            }

            bomCellRepository.saveAll(bomCellList);
            bomCellProduct.setVerify(true);
            bomCellProductRepository.save(bomCellProduct);
        }
    }

    @Override
    public List<String> getCellMtrl(String product) {
        return cellMpsModeRepository.getCellMtrlByProduct(product);
    }

    @Override
    public List<BomCell> getBomCell(String product) {
        return bomCellRepository.findByProduct(product);
    }


    @Override
    public Page<BomCellProduct> queryProduct(int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit,  Sort.Direction.ASC, "product");
        return bomCellProductRepository.findByProductLike(searchProduct+"%", pageable);
    }

    @Override
    public List<BomCellProduct> getBomCellProduct() {
        return bomCellProductRepository.findAll();
    }

    @Override
    public BomCellProduct getBomCellProduct(String product) {
        return bomCellProductRepository.findById(product).orElse(null);
    }

    @Override
    public void deleteBomCell(List<BomCell> list) {
        bomCellRepository.deleteAll(list);
    }

    @Override
    public void saveBomCell(List<BomCell> list) {
        bomCellRepository.saveAll(list);
    }
}
