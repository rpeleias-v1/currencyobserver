(* corebuild -pkg cohttp.async,yojson,textwrap currency.native *)
open Core.Std
open Async.Std
open Cohttp_async

let query_uri fromcur tocur =
  let base_uri = Uri.of_string "https://currencyconverter.p.mashape.com/" in
  Uri.add_query_params base_uri ["from", [fromcur]; "to", [tocur]; "from_amount", ["1"]]


let headers =
  Cohttp.Header.of_list ["X-Mashape-Key", "XMsVjlWYcomshoAojmOV1R4aPkmip1RSgyIjsntUwLB7yRRqvF";
                         "Accept", "application/json"]

let get_from_json json =
  match Yojson.Safe.from_string json with
  | `Assoc kv_list ->
    (match List.Assoc.find kv_list "to_amount" with
      | Some (`Float  i) -> Printf.printf "%f" i;
      | _  -> Printf.printf "Unable to find currency from USD to %s" "BRL")
  | _ -> Printf.printf "Body is not a valid json: %s" json


let get_currency fromcur tocur =
  Cohttp_async.Client.get ~headers:headers (query_uri fromcur tocur)
  >>= fun (_, body) ->
  Cohttp_async.Body.to_string body
  >>| fun body_text ->
  get_from_json body_text

let () =
  Command.async_basic
    ~summary:"Watches USD currency"
     Command.Spec.(
      empty +> anon ("currency" %: string)
     )
    (fun currency () -> get_currency currency "BRL")
    |> Command.run

(* let () = never_returns (Scheduler.go ()) *)
