import axios from "axios";
export default function post(url, param, callback) {
	const body = new URLSearchParams()
	body.append("data", JSON.stringify(param))
	axios.post(url, body)
		.then(function(back) {
			let res = back.data;
			callback(res)
		})
		.catch(function(e) {
			console.error(e);
		});
}
