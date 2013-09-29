import unittest
import math
import triangle
__author__ = 'datnt_55'
class TestTriangle(unittest.TestCase):

    # Test mien gia tri
    def testKhoangGiaTri1(self):
        self.assertEqual(triangle.detect_triangle(3,5,-1),"Ngoai khoang gia tri")
    def testKhoangGiaTri2(self):
        self.assertEqual(triangle.detect_triangle(math.pow(2,32)+1,math.pow(2,32)+1,math.pow(2,32)+1),"Ngoai khoang gia tri")
    def testKhoangGiaTri3(self):
        self.assertEqual(triangle.detect_triangle(math.pow(2,35)+1,math.pow(2,36)+1,1),"Ngoai khoang gia tri")
    def testKhoangGiaTri4(self):
        self.assertEqual(triangle.detect_triangle(-100,math.pow(2,36)+1,1),"Ngoai khoang gia tri")

    # Test kieu hop le
    def testKieuXacDinh1(self):
        self.assertEqual(triangle.detect_triangle(math.pow(2,33),math.pow(2,33),'vnu'),"Kieu khong hop le")
    def testKieuXacDinh2(self):
        self.assertEqual(triangle.detect_triangle(math.pow(2,33),'uet','vnu'),"Kieu khong hop le")
    def testKieuXacDinh3(self):
        self.assertEqual(triangle.detect_triangle('d','uet','vnu'),"Kieu khong hop le")
    def testKieuXacDinh4(self):
        self.assertEqual(triangle.detect_triangle('d',9,'vnu'),"Kieu khong hop le")

    #Test tam giac vuong
    def testTamGiacVuong1(self):
        self.assertEqual(triangle.detect_triangle(3,4,5),"Tam giac vuong")
    def testTamGiacVuong2(self):
        self.assertEqual(triangle.detect_triangle(3.0,2.0,math.sqrt(13)),"Tam giac vuong")
    def testTamGiacVuong3(self):
        self.assertEqual(triangle.detect_triangle(math.sqrt(13),math.sqrt(7),math.sqrt(20)),"Tam giac vuong")
    def testTamGiacVuong4(self):
        self.assertEqual(triangle.detect_triangle(math.sqrt(13),math.sqrt(12),5.0),"Tam giac vuong")

    #Test tam giac vuong can
    def testTamGiacVuongCan1(self):
        self.assertEqual(triangle.detect_triangle(1.0,1.0,math.sqrt(2)),"Tam giac vuong can")
    def testTamGiacVuongCan2(self):
        self.assertEqual(triangle.detect_triangle(2.0,math.sqrt(8),2.0),"Tam giac vuong can")
    def testTamGiacVuongCan3(self):
        self.assertEqual(triangle.detect_triangle(math.sqrt(5),math.sqrt(5),math.sqrt(10)),"Tam giac vuong can")
    def testTamGiacVuongCan4(self):
        self.assertEqual(triangle.detect_triangle(4.0,math.sqrt(8),math.sqrt(8) ),"Tam giac vuong can")

    #Test Tam giac deu
    def testTamGiacDeu1(self):
        self.assertEqual(triangle.detect_triangle(2**-30,2**-30,2**-30),"Tam giac deu")
    def testTamGiacDeu2(self):
        self.assertEqual(triangle.detect_triangle(7,7,7),"Tam giac deu")
    def testTamGiacDeu3(self):
        self.assertEqual(triangle.detect_triangle(2**32-2,2**32-2,2**32-2),"Tam giac deu")


    # Test tam giac thuong
    def testTamGiacThuong1(self):
        self.assertEqual(triangle.detect_triangle(2,5,6),"Tam giac thuong")
    def testTamGiacThuong2(self):
        self.assertEqual(triangle.detect_triangle(2**32-2, 2**3, 2**32-1),"Tam giac thuong")
    def testTamGiacThuong3(self):
        self.assertEqual(triangle.detect_triangle(7.3,6.1,8.9),"Tam giac thuong")
    def testTamGiacThuong4(self):
        self.assertEqual(triangle.detect_triangle(2,5,6),"Tam giac thuong")
        
    #Test khong phai tam giac
    def testNotTriangle1(self):
        self.assertEqual(triangle.detect_triangle(8.0,3.4,2**32-2),"Khong phai tam giac")
    def testNotTriangle2(self):
        self.assertEqual(triangle.detect_triangle(2,3,7),"Khong phai tam giac")
    def testNotTriangle3(self):
        self.assertEqual(triangle.detect_triangle(0.12,2**15,2**30),"Khong phai tam giac")
    def testNotTriangle4(self):
        self.assertEqual(triangle.detect_triangle(math.sqrt(6),math.sqrt(100),2),"Khong phai tam giac")

    #Test Tam giac can
    def testTamGiacCan1(self):
        self.assertEqual(triangle.detect_triangle(2,2,3),"Tam giac can")
    def testTamGiacCan2(self):
        self.assertEqual(triangle.detect_triangle(7.13, 7.13, 10),"Tam giac can")
    def testTamGiacCan3(self):
        self.assertEqual(triangle.detect_triangle(math.sqrt(7),math.sqrt(7),3),"Tam giac can")
if __name__ == '__main__':
    unittest.main()
