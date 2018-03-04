// Data Structures Homework 4
// Author : Shadasha Williams

#include <iostream>
#include <fstream>
#include <random>
#include <math.h>
#include <time.h>

using namespace std;

double Numbsteps;
double load_factor;
uint32_t NumbofRehash;
uint32_t T0[255], T1[255], T2[255], T3[255], T4[255], T5[255], T6[255], T7[255]; // arrays for the tabulation
uint32_t a_test; //int for the multiply shift
uint32_t b;
uint32_t n; //to measure size if table

//MARK: - structure to throw flags fro the distinct test types
struct key_parameter {
    uint32_t key;
    bool cuckoo_option;
    
    key_parameter(uint32_t k, bool cuckoo_opt) {
        key = k;
        cuckoo_option = cuckoo_opt;
    }
};

// create enumerators for each experiment run
enum Exp_Type { random_test, sequential_test };
enum Hash_function { tabulation, multiply_shift, naive_mod };

Exp_Type run;
Hash_function hash_type;

// define the hash tables via the line -D arg in the command line
//#define CUCKOO_HASH
#ifndef CUCKOO_HASH
#define LINEAR_PROB
#endif

/*
 * This is the xoroshiro128+ random generator, designed in 2016 by David Blackman
 * and Sebastiano Vigna, distributed under the CC-0 license. For more details,
 * see http://vigna.di.unimi.it/xorshift/.
 */
static uint64_t rng_state[2];

static uint64_t rng_rotl(const uint64_t x, int k)
{
    return (x << k) | (x >> (64 - k));
}

static uint64_t rng_next_u64(void)
{
    uint64_t s0 = rng_state[0], s1 = rng_state[1];
    uint64_t result = s0 + s1;
    s1 ^= s0;
    rng_state[0] = rng_rotl(s0, 55) ^ s1 ^ (s1 << 14);
    rng_state[1] = rng_rotl(s1, 36);
    return result;
}

static uint32_t rng_next_u32(void)
{
    return rng_next_u64() >> 11;
}

static void rng_setup(unsigned int seed)
{
    rng_state[0] = seed * 0xdeadbeef;
    rng_state[1] = seed ^ 0xc0de1234;
    for (int i=0; i<100; i++)
        rng_next_u64();
}

/*** The RNG ends here ***/



void initialize() {
    // initialize the random variables for the hash function
    a_test = rng_next_u32();
    b = rng_next_u32();
    // make the numbers odd
    if (a_test % 2 == 0) {
        a_test = a_test - 1;
    }
    if (b % 2 == 0) {
        b = b - 1;
    }
    
    for (int i = 0; i < 255; i++) {
        // do modular division with random int and the size of the table
        T0[i] = rng_next_u32() & (n - 1);
        T1[i] = rng_next_u32() & (n - 1);
        T2[i] = rng_next_u32() & (n - 1);
        T3[i] = rng_next_u32() & (n - 1);
        T4[i] = rng_next_u32() & (n - 1);
        T5[i] = rng_next_u32() & (n - 1);
        T6[i] = rng_next_u32() & (n - 1);
        T7[i] = rng_next_u32() & (n - 1);
    }
    
    
}
//MARK: - hash functions
uint32_t naive_modulo(key_parameter k) {
    return fmod(k.key, n);
}

uint32_t mult_shift(key_parameter k) {
    uint32_t num = 0;
    uint32_t vals;
    uint32_t l = log2(n);
    
    if(!k.cuckoo_option){
        vals = a_test*k.key;
        num = vals >> (32-l);
    }
    else {
        vals = b*k.key;
        num = vals >> (32-l);
    }
    //    if(num > n) {
    //        cout << num << " " << n << endl;
    //    }
    return num;
}


uint32_t tabulate(key_parameter par) {
    uint32_t key = par.key;
    uint32_t num = 0;
    uint32_t X[4];
    //set first shift to first byte
    for(int i = 0; i < 4; i++){
        X[i] = key & 0xFF;
        key >>= 8 ;
    }
    
    if (!par.cuckoo_option) {
        num = T0[X[0]] ^ T1[X[1]] ^ T3[X[2]] ^ T3[X[3]];
    } else {num = T4[X[0]] ^ T5[X[1]] ^ T6[X[2]] ^ T7[X[3]];}
    
    return num;
}

//MARK: - function for printing the time

double get_time(clock_t t) {
    t = clock() - t;
    // get time in nanoseconds
    double nanoseconds = ((double)t * 1000000000) / CLOCKS_PER_SEC;
    // print time in nanoseconds
    return nanoseconds;
}

// creates a hash with linear probing
class lp_hash_table{
    int numberOfElements;
    long int hash_size;
    
public:
    uint32_t *table;
    
    lp_hash_table(long int h_size) {
        hash_size = h_size;
        table = new uint32_t[h_size];
        fill_n(table, hash_size, 0);
        numberOfElements = 0;
        //set the table to null values
        
    }
    
    lp_hash_table() {
        hash_size = 1;
        table = new uint32_t[hash_size];
        fill_n(table, hash_size, 0);
        numberOfElements = 0;
        //set the table to null values
        
    }
    
    void init_table(long int h_size) {
        if (table) {delete [] table;}
        hash_size = h_size;
        table = new uint32_t[h_size];
        reset_table();
    }
    
    ~lp_hash_table() {
        //        if (table) {
        //            delete [] table;
        //            cout << "cleared table " << hash_size << endl;
        //        }
        //        numberOfElements = 0;
        //        hash_size = 0;
    }
    
    void insert_item(uint32_t key, function<uint32_t (key_parameter)> func) {
        
        uint32_t index = func(key_parameter(key, 0));
        // find the next free space in the table if place is filled
        while (table[index] != 0){
            // go to the next item
            index = (index + 1) % hash_size;
            Numbsteps++;
        }
        numberOfElements++;
        Numbsteps++;
        table[index] = key;
    }
    int get_hash_size() {
        return numberOfElements;
    }
    
    void reset_table() {
        this->numberOfElements = 0;
        fill_n(table, hash_size, 0);
    }
};

class cuckoo_hash_table {
    int hash_size;
    
public:
    
    uint32_t *table1;
    int size;
    cuckoo_hash_table(int hash_s) {
        size = 0;
        hash_size = hash_s;
        table1 = new uint32_t[hash_size];
        //set the table to null values
        std::fill(table1, table1 + sizeof( table1 ), 0 );
    }
    
    int get_hash_size() {
        return size;
    }
    
    void rehash(const function<uint32_t (key_parameter)> func) {
        // create new hash functions by intitializing the hash function values
        NumbofRehash++;
        if (NumbofRehash > 30842) { // constant value pulled from tests
            return;
        }
        initialize();
        uint32_t *temp = new uint32_t[hash_size];
        
        for (int i = 0; i < hash_size; i ++) {
            temp[i] = table1[i];
            table1[i] = 0;
        }
        
        for(int i = 0; i < hash_size; i++){
            if(temp[i] != 0) {
                // insert elements from the old hash to the new hash table
                uint32_t element = temp[i];
                cuckoo_insert(element, func);
            }
        }
        // copy the results from the temp to create new hash table
        delete [] temp;
        return;
        
    }
    
    void cuckoo_insert(int key, function<uint32_t (key_parameter)> func){
        uint32_t H1 = func(key_parameter(key, 0));
        uint32_t H2 = func(key_parameter(key, 1));
        int MAX_LOOP = 3 * log(hash_size*2)/log(1 + 0.6);
        // check if the object is already in the appropriate position in the table
        
        if(table1[H1] == key || table1[H2] == key ) {
            return;
        }
        uint32_t pos = H1;
        for(int i = 0; i < MAX_LOOP; i++) {
            // set the maximum loop to the size of the table
            if(table1[pos] == 0) {
                // if the hash function outputs an empty pos then insert and break
                table1[pos] = key;
                Numbsteps++;
                return;
            }
            uint32_t temp;
            temp = key;
            
            // if the position is not null then swap the occupants and make the new key
            // from the key that has been kicked out
            key = table1[pos];
            table1[pos] = temp;
            Numbsteps++;
            // move position to the next appropriate position from hash
            H1 = func(key_parameter(key, 0));
            H2 = func(key_parameter(key, 1));
            if(pos == H1) pos = H2;
            else pos = H1;
        }
        rehash(func);
        cuckoo_insert(key, func);
    }
    void reset_cuckoo_table() {
        this->size = 0;
        fill_n(table1, hash_size, 0);
    }
};

//MARK: - function to run the sequential test with linear probing
void run_seq(function<uint32_t (key_parameter)> func) {
    int pow_size = 10;
    double steps;
    double counter;
    double avg;
    
    for(int m = 0; m < 14; m++) {
        // outer loop to increase the size of n
        n = 1 << pow_size;
        lp_hash_table lp_hash;
        for(int j = 0; j < 100; j++) {
            // inner loop to run the test multiple times
            initialize();
            lp_hash.init_table(n);
            steps = 0;
            counter = 0;
            for (int x = 0; x < n; x++) {
                Numbsteps = 0;
                lp_hash.insert_item(x, func);
                load_factor = ((double)lp_hash.get_hash_size() / (double)n);
                if (load_factor >= .89 && load_factor <= .91) {
                    steps += Numbsteps;
                    counter++;
                }
                else if (load_factor > .91) {
                    break;
                }
            }
            
            avg = steps/counter;
            
            //            lp_hash.reset_table();
            cout << n << " " << avg << endl;
            avg = 0;
        }
        pow_size += 1;
    }
}

//MARK: - Function to run the random tests, uses the input from the

void run_random(function<uint32_t (key_parameter)> func) {
    double load_factor = 0;
    clock_t timer;
    double total_timer = 0;
    double total_steps = 0;
    // m represents the size of the hash table
    n = 1 << 20;
    NumbofRehash = 0;
    initialize();
#ifdef CUCKOO_HASH
    cuckoo_hash_table *rand_hash;
    rand_hash = new cuckoo_hash_table(n);
#endif
#ifdef LINEAR_PROB
    lp_hash_table *rand_hash;
    rand_hash = new lp_hash_table(n);
#endif
    cout << "NanoSeconds " << "Load_Factor " << " Number_of_Steps" << endl;
    int sub_seq = n / 100;
    for(int i = 0; i < n ; i++) {
        // fill the hash until the maximum hash size
        uint32_t x;
        x = rng_next_u32();
        
        timer = clock();
        if (NumbofRehash > log(n)/log(2)) {
            return;
        }
#ifdef LINEAR_PROB
        // insert in the appropriate table
        rand_hash->insert_item(x, func);
#endif
#ifdef CUCKOO_HASH
        rand_hash->cuckoo_insert(x, func);
        rand_hash->size++;
#endif
        total_timer += get_time(timer);
        total_steps += Numbsteps;
        // segement the inserts
        if (i % sub_seq == 0 & i != 0){
            int all_elements = rand_hash->get_hash_size();
            load_factor = ((double) all_elements / (double) n);
            cout << total_timer / 100 << " " << load_factor << " " << total_steps / 100 << " " << endl;
            total_steps = 0;
            total_timer = 0;
            NumbofRehash = 0;
        }
        
    }
}

//MARK: - Function to parse the enumerators from the command line

void init_enumerators(string exp_type, string hash_func) {
    run = exp_type == "RAND" ? random_test : sequential_test;
    if (hash_func == "TAB") {
        hash_type = tabulation;
    } else if (hash_func == "MULT") {
        hash_type = multiply_shift;
    } else if (hash_func == "MOD") {
        hash_type = naive_mod;
    }
}

//MARK: - Main function takes arguments from the command line to run the specified tests

int main(int argc, char *argv[] ) {
    rng_setup(39);
    // changing the function to tabular for sequential test
     if ( argc < 2) {
            cout << " Too few arguments for running the tests " << endl;
        }
    else {
    // initialize the command line inputs
    init_enumerators(argv[1], argv[2]);
    switch (run) {
        case sequential_test: {
            cout << "Starting the sequential test ";
            switch (hash_type) {
                case tabulation:{
                    cout << " using tabulation function " << endl;
                    function<uint32_t (key_parameter)> hash_func = tabulate;
                    run_seq(hash_func);
                    break;}
                case multiply_shift: {
                    cout << " using multiply function " << endl;
                    function<uint32_t (key_parameter)> hash_func1 = mult_shift;
                    run_seq(hash_func1);
                    break;}
                default:{
                    cout << " unsuitable input for hash function " << endl;
                    break;}
            }
            break;}
            
        case random_test: {
            switch (hash_type) {
                case tabulation: {
                    function<uint32_t (key_parameter)> hash_func = tabulate;
                    run_random(hash_func);
                    break;}
                case multiply_shift: {
                    function<uint32_t (key_parameter)> hash_func = mult_shift;
                    run_random(hash_func);
                    break;}
                case naive_mod: {
                    function<uint32_t (key_parameter)> hash_func = naive_modulo;
                    run_random(hash_func);
                    break;}
                    
                    
                default:{
                    break;}
            }
            
        }
        default:
            break;
    }
    
    }
    return 0;
}
