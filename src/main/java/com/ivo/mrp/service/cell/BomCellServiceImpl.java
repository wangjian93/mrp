package com.ivo.mrp.service.cell;

import com.ivo.common.BatchService;
import com.ivo.mrp.entity.MaterialGroup;
import com.ivo.mrp.entity.direct.cell.*;
import com.ivo.mrp.repository.cell.BomCellMaterialRepository;
import com.ivo.mrp.repository.cell.BomCellProductRepository;
import com.ivo.mrp.repository.cell.BomCellRepository2;
import com.ivo.mrp.repository.cell.CellMpsModeRepository;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private BomCellMaterialRepository bomCellMaterialRepository;

    private CellMpsModeRepository cellMpsModeRepository;

    private BomCellProductRepository bomCellProductRepository;

    private BatchService batchService;

    private BomCellRepository2 bomCellRepository2;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public BomCellServiceImpl(RestService restService, BomCellMaterialRepository bomCellMaterialRepository,
                              CellMpsModeRepository cellMpsModeRepository, BomCellProductRepository bomCellProductRepository,
                              BatchService batchService, BomCellRepository2 bomCellRepository2,
                              MaterialService materialService, MaterialGroupService materialGroupService) {
        this.restService = restService;
        this.bomCellMaterialRepository = bomCellMaterialRepository;
        this.cellMpsModeRepository = cellMpsModeRepository;
        this.bomCellProductRepository = bomCellProductRepository;
        this.batchService = batchService;
        this.bomCellRepository2 = bomCellRepository2;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    @Override
    public void syncBomCellMaterial() {
        log.info("同步CELL料号的B材料 >> START");
        //清空表
        bomCellMaterialRepository.truncateTable();

        List<Map> mapList = restService.getCellMtrl();
        if(mapList==null || mapList.size()==0) return;
        List<BomCellMaterial> bomCellMaterialList = new ArrayList<>();
        for(Map map : mapList) {
            BomCellMaterial bomCellMaterial = new BomCellMaterial();
            bomCellMaterial.setPLANT((String) map.get("PLANT"));
            bomCellMaterial.setPRODUCT((String) map.get("PRODUCT"));
            bomCellMaterial.setCELLMTRL((String) map.get("CELLMTRL"));
            bomCellMaterial.setMTRL_ID((String) map.get("MTRL_ID"));
            bomCellMaterial.setMEASURE_UNIT((String) map.get("MEASURE_UNIT"));
            bomCellMaterial.setUSAGEQTY( ((BigDecimal) map.get("USAGEQTY")).doubleValue() );
            bomCellMaterial.setSUBFLAG( ((BigDecimal) map.get("SUBFLAG")).intValue() );
            bomCellMaterial.setMEMO((String) map.get("MEMO"));
            bomCellMaterialList.add(bomCellMaterial);
        }
        batchService.batchInsert(bomCellMaterialList);
        log.info("同步CELL料号的B材料 >> END");
    }

    @Override
    public void syncCellMpsMode() {
        log.info("同步CELL的MPS机种命名 >> START");
        List<Map> mapList = restService.getCellMpsMode();
        if(mapList==null || mapList.size()==0) return;
        List<CellMpsMode> cellMpsModeList = new ArrayList<>();
        for(Map map : mapList) {
            CellMpsMode cellMpsMode = new CellMpsMode();
            cellMpsMode.setCellInput_pc((String) map.get("CellInPut_PC"));
            cellMpsMode.setCellMtrl((String) map.get("Material_FK"));
            cellMpsMode.setId(cellMpsMode.getCellInput_pc()+"_"+cellMpsMode.getCellMtrl());
            cellMpsModeList.add(cellMpsMode);
        }
        cellMpsModeRepository.saveAll(cellMpsModeList);
        log.info("同步CELL的MPS机种命名 >> END");
    }

    @Override
    public void syncBomCellProduct() {
        List<String> productList = cellMpsModeRepository.getProduct();
        if(productList == null || productList.size() == 0) return;
        for(String product : productList) {
            BomCellProduct bomCellProduct = bomCellProductRepository.findById(product).orElse(null);
            if(bomCellProduct == null) {
                bomCellProduct = new BomCellProduct();
                bomCellProduct.setProduct(product);
                bomCellProductRepository.save(bomCellProduct);
            }
        }
    }

    @Override
    public List<BomCellMaterial> getBomCellMaterial(List<String> cellMtrlList) {
        return bomCellMaterialRepository.findByCELLMTRLIn(cellMtrlList);
    }

    @Override
    public List<String> getCellMtrl(String product) {
        return cellMpsModeRepository.getCellMtrlByProduct(product);
    }

    @Override
    public List<BomCell2> getBomCell(String product) {
        return bomCellRepository2.findByProduct(product);
    }

    @Override
    public void test() {
        List<BomCellProduct> bomCellProductList = bomCellProductRepository.findAll();
        int i = bomCellProductList.size();
        for(BomCellProduct bomCellProduct : bomCellProductList) {
            System.out.println(i--);
            String product = bomCellProduct.getProduct();
            List<String> cellMtrlList = getCellMtrl(product);
            List<BomCellMaterial> bomCellMaterialList = getBomCellMaterial(cellMtrlList);

            if(bomCellMaterialList != null && bomCellMaterialList.size()>0) {
                List<BomCell2> bomCellList = new ArrayList<>();
                for(BomCellMaterial bomCellMaterial : bomCellMaterialList) {
                    BomCell2 bomCell = new BomCell2();
                    String material = bomCellMaterial.getMTRL_ID();
                    bomCell.setId(product+"_"+material);
                    bomCell.setProduct(product);
                    bomCell.setMaterial(material);
                    bomCell.setUsageQty(bomCellMaterial.getUSAGEQTY());
                    bomCell.setMeasureUnit(bomCellMaterial.getMEASURE_UNIT());

                    bomCell.setMaterialName(materialService.getMaterialName(material));
                    String materialGroup = materialService.getMaterialGroup(material);
                    bomCell.setMaterialGroup(materialGroup);
                    bomCell.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
                    bomCellList.add(bomCell);
                }

                bomCellRepository2.saveAll(bomCellList);
                bomCellProduct.setVerify(true);
                bomCellProductRepository.save(bomCellProduct);
            }
        }
    }
}