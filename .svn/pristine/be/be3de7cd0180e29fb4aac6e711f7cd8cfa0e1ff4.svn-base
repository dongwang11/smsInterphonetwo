#ifndef SOFT_AES_H
#define SOFT_AES_H

#include <stdint.h>

#define uint8 uint8_t

uint8 gadd(uint8 a, uint8 b);

uint8 gsub(uint8 a, uint8 b);

uint8 gmult(uint8 a, uint8 b);

void coef_add(uint8 a[], uint8 b[], uint8 d[]);

void coef_mult(uint8 *a, uint8 *b, uint8 *d);

uint8 * Rcon(uint8 i);

void add_round_key(uint8 *state, uint8 *w, uint8 r);

void mix_columns(uint8 *state);

void inv_mix_columns(uint8 *state);

void shift_rows(uint8 *state);

void inv_shift_rows(uint8 *state);

void sub_bytes(uint8 *state);

void inv_sub_bytes(uint8 *state);

void sub_word(uint8 *w);

void rot_word(uint8 *w);

void key_expansion(uint8 *key, uint8 *w);

extern void cipher(uint8 *in, uint8 *out, uint8 *key);

extern void inv_cipher(uint8 *in, uint8 *out, uint8 *key);

extern uint8* aes_init(uint8 *mac_id);
extern void aes_encode(uint8* in, uint8* out);
extern void aes_decode(uint8* in, uint8* out);
extern uint8 aes_key[16];

#endif