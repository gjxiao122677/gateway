package com.tsmc.gateway.modbusRTU;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.serial.SerialPortWrapper;


public class CollectionMain {
	private final static int SLAVE_ADDRESS = 1;
	private static final String COM = System.getenv("COM");
    private final static int BAUD_RATE = 9600;

    public static void main(String[] args) {
        SerialPortWrapper serialParameters = new
                SerialPortWrapperImpl(COM, BAUD_RATE, 8, 1, 0, 0, 0);

        ModbusFactory modbusFactory = new ModbusFactory();
        ModbusMaster master = modbusFactory.createRtuMaster(serialParameters);
        
        try {
            master.init();
            while(true) {
            	System.out.print("W1:");
            	System.out.println(readHoldingRegisters(master, SLAVE_ADDRESS, 1127, 1)*0.1);
            	
            	System.out.print("W2:");
            	System.out.println(readHoldingRegisters(master, SLAVE_ADDRESS, 1129, 1)*0.1);
            	System.out.print("W3:");
            	System.out.println(readHoldingRegisters(master, SLAVE_ADDRESS, 1131, 1)*0.1);
            	try {
            		Thread.sleep(5* 1000);
            	} catch (InterruptedException e) {
            		e.printStackTrace();
				}
            }
           
        } catch (ModbusInitException e) {
            e.printStackTrace();
        } finally {
            master.destroy();
        }
    }

    public static double readHoldingRegisters(ModbusMaster master, int slaveId, int start, int len) {
    	double res = -1;
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse)master.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            } else {
            	res = (double)(response.getShortData()[0]);

            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        return res;
    }
}
