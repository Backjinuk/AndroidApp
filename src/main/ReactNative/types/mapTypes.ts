interface Position {
    title?: string;
    address?: string;
    latitude: number;
    longitude: number;
  }
  interface Location {
    address: string;
    latitude: number;
    longitude: number;
    title: string;
  }
  interface Community {
      commuSeq : number
      commuWrite : string
      commuTitle : string
      commuComent : string
      totalUserCount : number
      applyStatus:string
      usercount : number
      latitude : number
      longitude : number
      address : string
      commuMeetingTime : Date
      regDt :Date
      upDt : Date
  }