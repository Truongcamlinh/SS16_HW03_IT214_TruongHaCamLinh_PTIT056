#!/usr/bin/env bash
set -euo pipefail

printf '\n1. Đọc lần đầu: cache miss, lấy từ DB và nạp Redis\n'
curl -sS http://localhost:8080/api/inventories/IPHONE-15

printf '\n\n2. Đọc lần hai: cache hit\n'
curl -sS http://localhost:8080/api/inventories/IPHONE-15

printf '\n\n3. Cập nhật DB lên 95 rồi evict cache\n'
curl -sS -X PUT http://localhost:8080/api/inventories/IPHONE-15 \
  -H 'Content-Type: application/json' -d '{"newQuantity":95}'

printf '\n\n4. Đọc lại: cache miss và nhận 95 từ DB\n'
curl -sS http://localhost:8080/api/inventories/IPHONE-15

printf '\n\n5. Số lượng âm phải trả HTTP 400\n'
curl -sS -i -X PUT http://localhost:8080/api/inventories/IPHONE-15 \
  -H 'Content-Type: application/json' -d '{"newQuantity":-10}'
printf '\n'
