package com.ivo.mrp.service.packageing;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.PackageSupplier;
import com.ivo.mrp.repository.packaging.PackageSupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class PackageSupplierServiceImpl implements PackageSupplierService {

    private PackageSupplierRepository packageSupplierRepository;

    @Autowired
    public PackageSupplierServiceImpl(PackageSupplierRepository packageSupplierRepository) {
        this.packageSupplierRepository = packageSupplierRepository;
    }

    @Override
    public List<PackageSupplier> getPackageSupplier(String month, String product, String type, String linkQty, String materialType) {
        return packageSupplierRepository.findByMonthAndProductLikeAndTypeLikeAndLinkQtyLikeAndMaterialType(month,"%"+product+"%",
                "%%", "%%",
                materialType);
    }

    @Override
    public void importExcel(InputStream inputStream, String fileName) {
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);

        List<String> title1 = new ArrayList<>();
        List<String> title2 = new ArrayList<>();

        List<Object> row1 = list.get(0);
        for(Object object : row1) {
            if(object == null) {
                title1.add(null);
            } else {
                title1.add(((String) object).trim());
            }
        }

        List<Object> row2 = list.get(1);
        for(Object object : row2) {
            if(object == null) {
                title2.add(null);
            } else {
                title2.add(((String) object).trim());
            }
        }


        List<PackageSupplier> packageSupplierList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for(int i=2; i<list.size(); i++) {
            List<Object> row = list.get(i);

            Object monthObject = row.get(0);
            String month;
            if(monthObject instanceof String) {
                month = (String) monthObject;
            } else if(monthObject instanceof BigDecimal) {
                BigDecimal b = (BigDecimal) monthObject;
                int days = b.intValue();//天数
                //获取时间
                Calendar c = Calendar.getInstance();
                c.set(1900, 0, 1);
                c.add(Calendar.DATE, days - 2);
                month = sdf.format(c.getTime());
            } else {
                month = sdf.format((Date) monthObject);
            }

            String product = (String) row.get(1);
            String type = (String) row.get(2);
            if(StringUtils.contains(type, "单")) {
                type = BomPackage.TYPE_D;
            } else {
                type = BomPackage.TYPE_L;
            }

            String linkQty = null;
            if(row.get(3) != null) {
                linkQty = (row.get(3)).toString();
            }

            String materialType = null;
            for(int j=4; j<title2.size(); j++) {
                if(StringUtils.isNotEmpty(title1.get(j))) {
                    materialType = title1.get(j);
                    if(StringUtils.containsIgnoreCase(materialType, PackageSupplier.MaterialType_BOX)) {
                        materialType = PackageSupplier.MaterialType_BOX;
                    } else {
                        materialType = PackageSupplier.MaterialType_TRAY;
                    }
                }

                String supplier = title2.get(j);

                double allocationRate = 0;
                if(row.size()>j && row.get(j) != null) {
                    allocationRate = ((BigDecimal)row.get(j)).doubleValue();
                }

                PackageSupplier packageSupplier = new PackageSupplier();
                packageSupplier.setMonth(month);
                packageSupplier.setProduct(product);
                packageSupplier.setType(type);
                packageSupplier.setLinkQty(linkQty);
                packageSupplier.setMaterialType(materialType);
                packageSupplier.setSupplierCode(supplier);
                packageSupplier.setSupplierName(supplier);
                packageSupplier.setAllocationRate(allocationRate);

                packageSupplierList.add(packageSupplier);
            }
        }
        packageSupplierList.size();
        packageSupplierRepository.saveAll(packageSupplierList);
    }
}
