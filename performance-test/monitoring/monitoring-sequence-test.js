import http from "k6/http";
import { sleep, check } from 'k6';
import { generateTokens } from './auth.js';

const host = "http://localhost:8080";
const lectureId = 1;
let imageIndex = 0;

export let options = {
    vus: 1,
    duration: '10s',
};

export function setup() {
    const tokens = generateTokens();
    return { tokens };
}

// 이미지를 순서대로 방출하는 시나리오
export default function ({tokens}) {
    const url = host + '/api/v1/monitoring/image/upload';

    const image = images[imageIndex++ % images.length]
    const randomToken = tokens[Math.floor(Math.random() * tokens.length)]

    console.log(`imageIndex: ${imageIndex}, image: ${image.title}`);

    const payload = JSON.stringify({
        lectureId: lectureId,
        encodedImageBase64: image.base64,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + randomToken
        },
    };

    let res = http.post(url, payload, params);

    check(res, {
        '학생 화면 이미지 업로드': (r) => r.status === 200,
    });

    sleep(1);
}

const images = JSON.parse(open('./sample-image-base64.json'));
