package com.test;

import com.saleoa.common.utils.DateUtil;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;
import com.saleoa.service.ISaleLogService;
import com.saleoa.service.ISaleLogServiceImpl;
import com.saleoa.service.ISaleService;
import com.saleoa.service.ISaleServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecoverSaleLog {
    private ISaleService saleService = new ISaleServiceImpl();
    private ISaleLogService saleLogService = new ISaleLogServiceImpl();

    public void execute() {
        List<SaleLog> logs = this.saleLogService.selectAll();
        List<Sale> sales = this.saleService.selectAll();
        for(Sale sale: sales) {
            Long saleId = sale.getId();
            Long lastSaleId = sale.getLastSaleId();
            Date createDate = sale.getCreateDate();
            Date nextSecCreateDate = DateUtil.addSecond(createDate, 1);
            if(lastSaleId.longValue() == 0l) {
                continue;
            }
            System.out.println("��ʼ��ѯlog");
            for(SaleLog log: logs) {
                if(log.getCreateDate().equals(createDate) || log.getCreateDate().equals(nextSecCreateDate)) {
                    if(log.getSaleId().longValue() == lastSaleId.longValue()) {
                        log.setSaleDate(sale.getSaleDate());
                    } else {
                        try {
                            System.out.println("��ʼ��ѯ��һ��");
                            Sale parent = this.saleService.selectById(lastSaleId);
                            while(parent.getLastSaleId().longValue() != 0) {
                                if(log.getSaleId().longValue() == parent.getLastSaleId().longValue()) {
                                    log.setSaleDate(sale.getSaleDate());
                                }
                                parent = this.saleService.selectById(parent.getLastSaleId());
                                System.out.println("��һ��");
                            }
                            System.out.println("������ѯ��һ��");
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    /*try {
                        System.out.println("��ʼ����log");
                        this.saleLogService.update(log);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }*/
                }
            }
            System.out.println("��ѯlog����");
        }
        System.out.println("����");
        System.out.println("��ʼ��������log");
        try {
            List<SaleLog> updates = new ArrayList<SaleLog>();
            for(int i = 0; i < logs.size(); i ++) {
                if(i > 0 && i % 100 == 0) {
                    System.out.println("100����ʼ����");
                    this.saleLogService.updateBatch(updates);
                    updates = new ArrayList<SaleLog>();
                    System.out.println("100�����½���");
                }
                updates.add(logs.get(i));
            }
            System.out.println("ʣ��"+updates.size()+"����ʼ����");
            this.saleLogService.updateBatch(updates);
            System.out.println("ʣ��"+updates.size()+"���������");
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("��������log����");
        }
    }

    public static void main(String[] args) {
        RecoverSaleLog recoverSaleLog = new RecoverSaleLog();
        recoverSaleLog.execute();
        //int i = 2000;
        //System.out.println(i%1000);
    }

}
