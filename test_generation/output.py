import input
from random import randint
import itertools
import unittest

arr =[]                                                         # Mang luu cac gia tri random
kiemtra = True                                                  #Bien kiem tra lop tuong duong

# Ham kiem tra lop tuong duong
def testtuongduong(array1):
    kt = True
    i = 1
    while i < len(array1)-2:
        j = i+1
        while j < len(array1):
            if array1[i] - array1[j] >= 0:
                if array1[i-1] - array1[j+1] > 0:
                    kt =True
                else:
                    kt = False
                    return kt
            j = j + 2
        i = i + 2
    return kt
#----------------------------------------------------------------------------------------------------------
# Ham lay gia tri random trong cac khoang tuong duong
def randomList(listrand):
    mangRandom =[]
    for i in range(len(listrand)):
        mang =[]
        for j in range(len(listrand[i])):
            mang.append(randint(listrand[i][j][0],listrand[i][j][1]))
        mangRandom.append(mang)
    return mangRandom
#-----------------------------------------------------------------------------------------------------------
doc5 =[]
doc4 = []
myArray = []
# Lay du lieu tu docstring
doc = input.main.__doc__.split("\n")
for i in range(len(doc)-1):
    if (i>0) :
        dem = 0
        for j in range(len(doc[i])):
            if (doc[i][j] == "[") :
                dem = dem +1
        myArray.append(dem)

for a in range(len(doc)):
    doc2 = doc[a].split("[")
    for i in range(len(doc2)):
        if (i>0):
            doc3 = doc2[i].split("]")
            for j in range(len(doc3)):
                tmp = doc3[j].split(";")
                for u in range(len(tmp)):
                        doc4.append(tmp[u])
length = range(len(doc4))
for t in length:
    if (t%3 != 2 ):
        doc5.append(doc4[t])
tmp =0
j=0
h=0
luu = 0
arrayluu =[]
for i in range(len(myArray)):
    array1=[]
    while j < len(doc5):
        if (tmp < myArray.__getitem__(i)*2):
            array1.append(int(doc5[j]))
            tmp = tmp +1
            j = j+1
        if (tmp == myArray.__getitem__(i)*2):
            luu = luu +tmp
            j = luu
            tmp=0
            break
    arrayluu.append(array1)
# Kiem tra lop tuong duong
for i in range(len(arrayluu)):
    if  testtuongduong(arrayluu[i]) == False:
        kiemtra = False
# Luu cac lop tuong duong ra mang
arraytam = []
for i in range(len(arrayluu)):
    arraytmp = []
    SplitInto= lambda L, n: [L[i:i+n] for i in range(0, len(L), n)]
    arraytmp = SplitInto(arrayluu[i], 2)
    arraytam.append(arraytmp)

arr = randomList(arraytam)
# Sinh testCase tu dong
class output(unittest.TestCase):
    pass
def test_generator(*args):
    def test(self):
        self.assertEqual(input.main(*args),1)
    return test
if __name__ == '__main__':
    if kiemtra == False:
         raise Exception("wrong input")
    else:
        i = 1
        for element in itertools.product(*arr):
            test_name = 'test_'+str(i)+' '+ str(element)
            test = test_generator(*element)
            setattr(output, test_name, test)
            i = i+1
        unittest.main()
