#COMP9318 Projest 2019 T1
#Team Name: New Wine
#Student Name & Number: Yuting GUO (z5144431)

import numpy as np
import re
import submission as submission

def read_file(file):
    file_dict = {}#
    value_dict = {}
    key = 0
    value_dict[key] = {}
    d = {}
    with open(file,'r') as file_name:
        #先获取文件中第一行即总行数
        num = int(file_name.readline().strip())
        #逐行读文件
        for i in range(num):
            line = file_name.readline().strip()
            file_dict[line] = i
        #将下面的value存为字典  
        while True:
            line = file_name.readlines()
            #print (line)
            if not line:
                break           
            for i in range(len(line)):
                line[i] = list(map(int, line[i].strip('\n').split(' ')))
                key = line[i][0]
                if i > 0:
                    if key != line[i - 1][0]:
                        value_dict[key] = {}
                value_dict[key][line[i][1]] = line[i][2]
            #print (value_dict)

    return file_dict, value_dict, num
                
def make_matrix(state_num, symbol_num, state_dict, symbol_dict,smooth,transmission_dict,emission_dict):
    A_matrix = np.zeros((state_num, state_num),float)
    #global begin_index
    begin_index = state_dict['BEGIN']
    #global end_index
    end_index = state_dict['END']
    #print(begin_index)
    #print(end_index)
    for i in range(state_num):
        for j in range(state_num):
            if j == begin_index or i == end_index:
                #print(i,j)
                A_matrix[i][j] = 0
                continue
            else:
                if i in transmission_dict:
                    ni = sum(list(transmission_dict[i].values()))
                    if j in transmission_dict[i]:
                        nij = transmission_dict[i][j]
                        A_matrix[i][j] = (nij + smooth) / (smooth*state_num + ni - 1)
                    else:
                        A_matrix[i][j] = smooth / (smooth*state_num + ni - 1)
        if i == begin_index:
            begin_matrix = A_matrix[i,:]

    #print(A_matrix)
    B_matrix = np.zeros((state_num,symbol_num+1),float)
    for i in range(state_num):
        #print(i)
        if i in emission_dict:
            mi = sum(list(emission_dict[i].values()))
            for j in range(symbol_num + 1):
                #print(j)
                if j == begin_index or j == end_index:
                    B_matrix[i][j] = 0
                if j in emission_dict[i]:
                    mij = emission_dict[i][j]
                    B_matrix[i][j] = (mij + smooth) / (smooth * symbol_num + mi + 1)
                else:
                    B_matrix[i][j] = smooth / (smooth * symbol_num + mi + 1)

    #print(B_matrix)
    #print (len(begin_matrix))

    return A_matrix, B_matrix, begin_matrix

def open_query(Query_File,symbol_num,symbol_dict):
    pattern = r"[0-9A-Za-z.]+|[,&-/()]"
    query_list = []
    with open(Query_File, 'r') as query_f:
        while True:
            line = query_f.readline().strip()
            #print(line)
            if not line:
                break
            query = re.compile(pattern).findall(line)
            symbol_dict["UNK"] = symbol_num

            query_list.append(query)
            
    return query_list

#def calculate()

def viterbi(state_num, state_dict, symbol_dict,B_matrix,A_matrix, begin_matrix,query_list, k):
    final_result = []
    for query in query_list:
        #print(query)
        viterbi_list = []

        query_plus = len(query)+2
        list_plus = [0.0,[]]
        #print (query_matrix_list)
        for i in range(state_num):
            viterbi_list.append([])
            for j in range(query_plus):
                viterbi_list[i].append([])
                for l in range(k):
                    viterbi_list[i][j].append([])
                    viterbi_list[i][j][l].append(0.0)
                    viterbi_list[i][j][l].append([])

        begin_index = state_dict['BEGIN']
        end_index = state_dict['END']
        begin_value = [1,[]]
        viterbi_list[begin_index][0][0] = begin_value
        first_query = query[0]
        for i in range(state_num):
            if first_query in symbol_dict.keys():
                viterbi_list[i][1][0][0] = begin_matrix[i] * B_matrix[i, symbol_dict[first_query]]
            else:
                viterbi_list[i][1][0][0] = begin_matrix[i] * B_matrix[i, symbol_dict['UNK']]

            viterbi_list[i][1][0][1].append(begin_index)



        for j in range(2, query_plus - 1):
            for i in range(state_num):
                temp = []
                for m in range(state_num):
                    for l in range(k):
                        if query[j - 1] in symbol_dict.keys():
                            temp.append((viterbi_list[m][j - 1][l][0] * A_matrix[m, i] * B_matrix[i, symbol_dict[query[j-1]]],viterbi_list[m][j - 1][l][1]+[m]))
                        else:
                            temp.append((viterbi_list[m][j - 1][l][0] * A_matrix[m, i] * B_matrix[i, symbol_dict['UNK']], viterbi_list[m][j - 1][l][1]+[m]))
                temp = sorted(temp,key = lambda x:x[0],reverse = True)
                for n in range(k):
                    viterbi_list[i][j][n][0] = temp[n][0]
                    viterbi_list[i][j][n][1].extend(temp[n][1])

        for i in range(state_num):
            for j in range(k):
                end_list = A_matrix[:, end_index]
                viterbi_list[i][query_plus-1][j][0] = end_list[i] * viterbi_list[i][query_plus-2][j][0]
                viterbi_list[i][query_plus-1][j][1].extend(viterbi_list[i][query_plus-2][j][1] + [i])

        r = []

        for i in range(state_num):
            r.extend(viterbi_list[i][query_plus-1])
        r = sorted(r, key=lambda x: x[0], reverse=True)
        result_l = []
        #end = state_dict['END']
        for i in range(k):
            result = r[i][1] + [end_index] + [np.log(r[i][0])]
            result_l.append(result)
            # print(result_l)

        final_result.extend(result_l)
    return final_result

# Question 1
def viterbi_algorithm(State_File, Symbol_File, Query_File):# do not change the heading of the function
    state_dict, transmission_dict, state_num = read_file(State_File)
    symbol_dict, emission_dict, symbol_num = read_file(Symbol_File)

    query_list = open_query(Query_File,symbol_num,symbol_dict)
    smooth = 1
    A_matrix, B_matrix, begin_matrix = make_matrix(state_num, symbol_num, state_dict, symbol_dict, smooth,transmission_dict,emission_dict)
    k = 1
    q1 = viterbi(state_num, state_dict, symbol_dict, B_matrix, A_matrix, begin_matrix, query_list, k)

    return q1



# Question 2
def top_k_viterbi(State_File, Symbol_File, Query_File, k): # do not change the heading of the function
    state_dict, transmission_dict, state_num = read_file(State_File)
    symbol_dict, emission_dict, symbol_num = read_file(Symbol_File)
    query_list = open_query(Query_File, symbol_num, symbol_dict)
    smooth = 1
    A_matrix, B_matrix, begin_matrix = make_matrix(state_num, symbol_num, state_dict, symbol_dict, smooth,
                                                   transmission_dict, emission_dict)
    q2 = viterbi(state_num, state_dict, symbol_dict, B_matrix, A_matrix, begin_matrix, query_list, k)
    return q2

# Question 3 + Bonus
def advanced_decoding(State_File, Symbol_File, Query_File): # do not change the heading of the function
    pass # Replace this line with your implementation...

State_File ='State_File'
Symbol_File='Symbol_File'
Query_File ='Query_File'

k = 200
top_k_result = submission.top_k_viterbi(State_File, Symbol_File, Query_File, k)

for row in top_k_result:
    print(row)
