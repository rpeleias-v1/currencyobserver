(* corebuild -pkg cohttp.async,yojson,textwrap currency.native *)
open Core.Std
open Async.Std
open Cohttp_async

type last_currency_type = {
  mutable fromcur : string;
  mutable tocur : string;
  mutable value : float option;
  mutable last_execution : Time.t;
}

let last_currency = {
  fromcur = "USD";
  tocur = "BRL";
  value = None;
  last_execution = Time.now ()
};;

let query_uri fromcur tocur =
  let base_uri = Uri.of_string "https://currencyconverter.p.mashape.com/" in
  Uri.add_query_params base_uri ["from", [fromcur]; "to", [tocur]; "from_amount", ["1"]]


let headers =
  Cohttp.Header.of_list ["X-Mashape-Key", "XMsVjlWYcomshoAojmOV1R4aPkmip1RSgyIjsntUwLB7yRRqvF";
                         "Accept", "application/json"]

let process_currency current =
  let execution = Time.now () in
  match last_currency.value with
  | None ->
    last_currency.value <- (Some current);
    last_currency.last_execution <- execution;
    Printf.printf "First value to hipchat %f\n" current
  | Some i ->
    last_currency.value <- (Some current);
    last_currency.last_execution <- execution;
    Printf.printf "Current Value updated %f" current

let get_from_json json =
  match Yojson.Safe.from_string json with
  | `Assoc kv_list ->
    (match List.Assoc.find kv_list "to_amount" with
      | Some (`Float  i) -> process_currency i;
      | _  -> Printf.printf "Unable to find currency from USD to %s" "BRL")
  | _ -> Printf.printf "Body is not a valid json: %s" json


let get_currency () =
  Cohttp_async.Client.get ~headers:headers (query_uri last_currency.fromcur last_currency.tocur)
    >>= fun (_, body) ->
    Cohttp_async.Body.to_string body
    >>| fun body_text ->
    get_from_json body_text


let () =
  Command.async_basic
    ~summary:"Watches USD currency"
     Command.Spec.(
      empty
      +> anon ("fromcur" %: string)
      +> anon ("tocur" %: string)
     )(
       fun fromcur tocur () ->
       last_currency.fromcur <- fromcur;
       last_currency.tocur <- tocur;
       get_currency ()

     )
    |> Command.run

let () = never_returns (Scheduler.go ())
